package com.mcc.fshopper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.mcc.fshopper.R;
import com.mcc.fshopper.adapter.CommentListAdapter;
import com.mcc.fshopper.adapter.ProductSliderAdapter;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.data.sqlite.WishDBController;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.Comment;
import com.mcc.fshopper.model.OrderModel;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.network.parser.CommentsParser;
import com.mcc.fshopper.network.parser.ParserKey;
import com.mcc.fshopper.network.parser.ProductParser;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AppUtility;
import com.mcc.fshopper.utils.RVEmptyObserver;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Nasir on 5/17/17.
 */

public class ProductDetailsActivity extends BaseActivity {

    // init variables
    private Context mContext;
    private Activity mActivity;

    // init view
    private TextView tvProductName, emptyView, tvSalesPrice, tvLikeCounter, tvDescription, tvContactNumber, tvOrder;
    private RelativeTimeTextView tvTimeStamp;
    private LinearLayout btnOrder, btnMessenger, lytOrderNote;
    private FloatingActionButton fabCall, fabMessage;
    private ImageButton btnShareProduct, addWishList, searchButton;
    private WebView wvDescription;
    private EditText etOrderNote;

    private ViewPager mPager;
    private ProductSliderAdapter productSliderAdapter;

    // recycler view
    private ArrayList<Comment> commentList;
    private RecyclerView rvComments;
    private CommentListAdapter commentListAdapter;
    private LinearLayoutManager commentsLytManager;

    private boolean addedToWishList;

    private ProductModel product = null;
    private String sellerId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        initToolbar();
        loadFbProduct();
        initListener();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = ProductDetailsActivity.this;
        commentList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_product_details);

        initToolbar();
        enableBackButton();
        initLoader();

        tvProductName = (TextView) findViewById(R.id.tvProductName);
        tvTimeStamp = (RelativeTimeTextView) findViewById(R.id.tvTimeStamp);
        tvSalesPrice = (TextView) findViewById(R.id.tvSalesPrice);
        tvLikeCounter = (TextView) findViewById(R.id.tvLikeCounter);
        tvContactNumber = (TextView) findViewById(R.id.tv_contact_number);
        //tvDescription = (TextView) findViewById(R.id.tv_description);
        wvDescription = (WebView) findViewById(R.id.wvDescription);
        mPager = (ViewPager) findViewById(R.id.vpImageSlider);
        fabCall = (FloatingActionButton) findViewById(R.id.fab_call);
        fabMessage = (FloatingActionButton) findViewById(R.id.fab_message);
        btnMessenger = (LinearLayout) findViewById(R.id.btn_messenger);
        btnOrder = (LinearLayout) findViewById(R.id.btn_order);
        btnShareProduct = (ImageButton) findViewById(R.id.imgBtnShare);
        addWishList = (ImageButton) findViewById(R.id.addWishList);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        lytOrderNote = (LinearLayout) findViewById(R.id.lyt_order_note);
        etOrderNote = (EditText) findViewById(R.id.et_order_note);
        tvOrder = (TextView) findViewById(R.id.tv_order);


        // init review list
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        commentsLytManager = new LinearLayoutManager(mActivity);
        rvComments.setLayoutManager(commentsLytManager);
        commentListAdapter = new CommentListAdapter(commentList);
        emptyView = (TextView) findViewById(R.id.emptyView);
        commentListAdapter.registerAdapterDataObserver(new RVEmptyObserver(rvComments, emptyView));
        rvComments.setAdapter(commentListAdapter);

        wvDescription.getSettings();
        wvDescription.getSettings().setTextZoom(85);
        wvDescription.setBackgroundColor(Color.TRANSPARENT);
        wvDescription.setVerticalScrollBarEnabled(false);
    }


    private void loadFbProduct() {

        tvContactNumber.setText(AppConstants.CALL_NUMBER);

        // get product id
        Intent intent = getIntent();
        String postId = intent.getStringExtra(AppConstants.PRODUCT_ID);
        sellerId = intent.getStringExtra(AppConstants.SELLER_ID);

        // set post details
        loadPostDetails(postId);
        // load comments
        loadPostComments(postId);
    }

    private void loadPostDetails(String postId) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + postId,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        product = new ProductParser().getProduct(response.getRawResponse());

                        if (product != null) {
                            // update wish button
                            updateWishButton(product.getId());
                            // set image
                            loadFbPhoto(product.getImageList());
                            // set other properties
                            tvProductName.setText(product.getTitle());
                            if (product.getUpdateDate() != null) {
                                tvTimeStamp.setReferenceTime(AppUtility.getTimeAgo(product.getUpdateDate()));
                            } else {
                                tvTimeStamp.setVisibility(View.INVISIBLE);
                            }
                            tvLikeCounter.setText(String.valueOf(product.getLikeCount()));
                            tvSalesPrice.setText(product.getPrice());
                            String description = product.getDetails();
                            if(description != null) {
                                description = description.replaceAll("\n", "<br />");
                            }
                            wvDescription.loadData(description, "text/html; charset=utf-8", "UTF-8");
                        }

                        // hide loader
                        hideLoader();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,message,name,link,full_picture,attachments, type,likes.limit(0).summary(true),created_time,updated_time");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void initListener() {

        btnShareProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtility.shareProduct(mActivity, product.getLink());

            }
        });

        addWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product != null) {
                    toggleWishList();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.getInstance().invokeFbSearchListActivity(mActivity, AppConstants.EMPTY_STRING);
            }
        });

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);

            }
        });
        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtility.sendSMS(mActivity, AppConstants.CALL_NUMBER, AppConstants.CONTACT_TEXT);
            }
        });

        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtility.invokeMessengerBot(mActivity, sellerId);

            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderNoteDialog();
/*                if (lytOrderNote.getVisibility() == View.GONE) {
                    lytOrderNote.setVisibility(View.VISIBLE);
                    tvOrder.setText(getString(R.string.order_now));
                } else {
                    String productOrderNote = etOrderNote.getText().toString();

                    if (productOrderNote.isEmpty()){
                        AppUtility.showToast(mContext, getString(R.string.note_empty_msg));
                    }
                    else {
                        lytOrderNote.setVisibility(View.GONE);
                        tvOrder.setText(getString(R.string.order));

                        OrderModel orderModel = new OrderModel(tvProductName.getText().toString(), productOrderNote);
                        ActivityUtils.getInstance().invokeAddressActivity(mActivity, orderModel, false, true);
                    }
                }*/

            }
        });

    }

    private void loadFbPhoto(final ArrayList<String> imageList) {

        if (imageList != null && !imageList.isEmpty()) {
            productSliderAdapter = new ProductSliderAdapter(mContext, imageList);
            mPager = (ViewPager) findViewById(R.id.vpImageSlider);
            mPager.setAdapter(productSliderAdapter);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.sliderIndicator);
            indicator.setViewPager(mPager);

            productSliderAdapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemListener(View view, int position) {
                    ActivityUtils.getInstance().invokeImage(mActivity, imageList.get(position));
                }
            });
        }
    }


    private void updateWishButton(String prodId) {
        try {
            WishDBController wishController = new WishDBController(mContext);
            wishController.open();
            addedToWishList = wishController.isAlreadyWished(prodId);
            wishController.close();

            if (addedToWishList) {
                addWishList.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_wish));
            } else {
                addWishList.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_un_wish));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void toggleWishList() {
        if (!addedToWishList) {
            try {
                WishDBController wishController = new WishDBController(mContext);
                wishController.open();
                wishController.insertWishItem(product.getId(), product.getTitle(), product.getImageList().get(AppConstants.INDEX_ZERO), product.getPrice(),
                        0, product.getLikeCount());
                wishController.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                WishDBController wishController = new WishDBController(mContext);
                wishController.open();
                wishController.deleteWishItemById(product.getId());
                wishController.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateWishButton(product.getId());
    }

    private void loadPostComments(String postId) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + postId + "/" + ParserKey.KEY_COMMENTS,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                commentList.addAll(new CommentsParser().getComments(response.getRawResponse()));
                                if (commentList.size() > 0) {
                                    commentListAdapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e){
                            }
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString(ParserKey.KEY_LIMIT, AppConstants.COMMENT_LIMIT_VALUE);
        request.setParameters(parameters);
        request.executeAsync();
    }
   private void orderNoteDialog(){
       LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mActivity);
       View mView = layoutInflaterAndroid.inflate(R.layout.order_note_dialog, null);
       AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mActivity);
       alertDialogBuilderUserInput.setView(mView);

       final EditText edtOrderNote = (EditText) mView.findViewById(R.id.et_order_note);
       alertDialogBuilderUserInput
               .setCancelable(false)
               .setPositiveButton(getString(R.string.order_now), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialogBox, int id) {
                       String productOrderNote = edtOrderNote.getText().toString();

                       if (productOrderNote.isEmpty()){
                           AppUtility.showToast(mContext, getString(R.string.note_empty_msg));
                       }
                       else {
                           OrderModel orderModel = new OrderModel(tvProductName.getText().toString(), productOrderNote);
                           ActivityUtils.getInstance().invokeAddressActivity(mActivity, orderModel, false, true);
                       }
                   }
               })

               .setNegativeButton("Cancel",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialogBox, int id) {
                               dialogBox.cancel();
                           }
                       });

       AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
       alertDialogAndroid.show();
   }
}
