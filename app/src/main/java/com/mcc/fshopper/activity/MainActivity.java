package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.mcc.fshopper.R;
import com.mcc.fshopper.adapter.ProductListAdapter;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.data.sqlite.NotificationDBController;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.NotificationModel;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.network.helper.ProductLoader;
import com.mcc.fshopper.network.parser.ProductParser;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AdUtils;
import com.mcc.fshopper.utils.AnalyticsUtils;
import com.mcc.fshopper.utils.AppUtility;
import com.mcc.fshopper.utils.ListTypeShow;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    // variables
    private Context mContext;
    private Activity mActivity;

    private static final int COLUMN_SPAN_COUNT = 2;
    private ArrayList<ProductModel> sellItemList, postItemList, popularItemList;
    private ProductListAdapter sellItemAdapter, postItemAdapter, popularItemAdapter;

    // ui declaration
    private RelativeLayout sellItemParent, postItemParent, popularItemParent;
    private TextView tvSellListTitle, tvSellItemAll, tvPostItemTitle, tvPostItemAll, tvPopularListTitle, tvPopularItemAll, tvNotificationCounter;
    private ImageView imgNotification, ivSearchIcon;
    private EditText edtSearchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        initListener();
        getPageFeed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotificationCounter();

        // load full screen ad
        AdUtils.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        sellItemList = new ArrayList<>();
        postItemList = new ArrayList<>();
        popularItemList = new ArrayList<>();

        // analytics event trigger
        AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("Main Activity");
    }

    private void initView() {

        // set parent view
        setContentView(R.layout.activity_main);

        // initiate drawer and toolbar
        initToolbar();
        initDrawer();
        initLoader();

        imgNotification = (ImageView) findViewById(R.id.imgNotification);
        ivSearchIcon = (ImageView) findViewById(R.id.ivSearchIcon);
        tvNotificationCounter = (TextView) findViewById(R.id.tvNotificationCounter);
        edtSearchProduct = (EditText) findViewById(R.id.edtSearchProduct);

        // sell item ui
        RelativeLayout lytFeaturedList = (RelativeLayout) findViewById(R.id.lytSellItems);
        RecyclerView rvSellItems = (RecyclerView) lytFeaturedList.findViewById(R.id.homeRecyclerView);
        tvSellListTitle = (TextView) lytFeaturedList.findViewById(R.id.tvListTitle);
        tvSellItemAll = (TextView) lytFeaturedList.findViewById(R.id.tvSeeAll);
        sellItemParent = (RelativeLayout) lytFeaturedList.findViewById(R.id.parentPanel);
        // init sell item RecyclerView
        rvSellItems.setLayoutManager(new GridLayoutManager(mActivity, COLUMN_SPAN_COUNT));
        sellItemAdapter = new ProductListAdapter(mContext, sellItemList, ListTypeShow.GRID);
        rvSellItems.setAdapter(sellItemAdapter);

        // post item ui
        RelativeLayout lytPostItem = (RelativeLayout) findViewById(R.id.lytPostItems);
        RecyclerView rvPostItems = (RecyclerView) lytPostItem.findViewById(R.id.homeRecyclerView);
        tvPostItemTitle = (TextView) lytPostItem.findViewById(R.id.tvListTitle);
        tvPostItemAll = (TextView) lytPostItem.findViewById(R.id.tvSeeAll);
        postItemParent = (RelativeLayout) lytPostItem.findViewById(R.id.parentPanel);
        // init post item RecyclerView
        rvPostItems.setLayoutManager(new GridLayoutManager(mActivity, COLUMN_SPAN_COUNT));
        postItemAdapter = new ProductListAdapter(mContext, postItemList, ListTypeShow.GRID);
        rvPostItems.setAdapter(postItemAdapter);

        // popular list ui
        RelativeLayout lytPopularList = (RelativeLayout) findViewById(R.id.lytPopularItems);
        RecyclerView rvPopularItems = (RecyclerView) lytPopularList.findViewById(R.id.homeRecyclerView);
        tvPopularListTitle = (TextView) lytPopularList.findViewById(R.id.tvListTitle);
        tvPopularItemAll = (TextView) lytPopularList.findViewById(R.id.tvSeeAll);
        popularItemParent = (RelativeLayout) lytPopularList.findViewById(R.id.parentPanel);
        // init popular items RecyclerView
        rvPopularItems.setLayoutManager(new GridLayoutManager(mActivity, COLUMN_SPAN_COUNT));
        popularItemAdapter = new ProductListAdapter(mContext, popularItemList, ListTypeShow.GRID);
        rvPopularItems.setAdapter(popularItemAdapter);


        // Check network connection
        AppUtility.noInternetWarning(findViewById(R.id.parentPanel), mContext);
        if (!AppUtility.isNetworkAvailable(mContext)) {
            showEmptyView();
        }
    }


    private void initListener() {


        sellItemAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {

                ActivityUtils.getInstance().invokeProductDetails(mActivity, sellItemList.get(position).getId(), sellItemList.get(position).getSellerId());
            }
        });
        postItemAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {

                ActivityUtils.getInstance().invokeProductDetails(mActivity, postItemList.get(position).getId(), postItemList.get(position).getSellerId());

            }
        });
        popularItemAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {

                ActivityUtils.getInstance().invokeProductDetails(mActivity, popularItemList.get(position).getId(), popularItemList.get(position).getSellerId());

            }
        });

        // toolbar notification action listener
        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdThenActivity(NotificationActivity.class);

                /**
                 * if you don't want to show notification then disable
                 * disable previous line and use line given bellow
                 */
                //ActivityUtils.getInstance().invokeActivity(mActivity, NotificationActivity.class, false);

            }
        });

        // search icon at home action listener
        ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearchProduct.getText().toString().isEmpty()) {
                    AppUtility.showToast(mContext, getString(R.string.type_something));
                } else {
                    ActivityUtils.getInstance().invokeFbSearchListActivity(mActivity, edtSearchProduct.getText().toString());
                }
            }
        });


        edtSearchProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (edtSearchProduct.getText().toString().isEmpty()) {
                        AppUtility.showToast(mContext, getString(R.string.type_something));
                    } else {
                        ActivityUtils.getInstance().invokeFbSearchListActivity(mActivity, edtSearchProduct.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        // See all listener
        tvSellItemAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().invokeFbProducts(mActivity, getString(R.string.sell_item), AppConstants.LIST_TYPE_SELL);
            }
        });

        tvPostItemAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().invokeFbProducts(mActivity, getString(R.string.post_item), AppConstants.LIST_TYPE_POST);
            }
        });

        tvPopularItemAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().invokeFbProducts(mActivity, getString(R.string.popular_item), AppConstants.LIST_TYPE_POPULAR);
            }
        });

    }

    private void loadNotificationCounter() {
        try {
            NotificationDBController notifyController = new NotificationDBController(mContext);
            notifyController.open();
            ArrayList<NotificationModel> notifyList = notifyController.getUnreadNotification();
            notifyController.close();

            if (notifyList.isEmpty()) {
                tvNotificationCounter.setVisibility(View.GONE);
            } else {
                tvNotificationCounter.setVisibility(View.VISIBLE);
                tvNotificationCounter.setText(String.valueOf(notifyList.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvNotificationCounter.setVisibility(View.GONE);
        }
    }


    private void getPageFeed() {

        showLoader();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,message,name,link,attachments, type,likes.limit(0).summary(true),created_time,updated_time,from");

        GraphRequest graphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + getString(R.string.facebook_page_id) + "/feed?limit=" + AppConstants.POST_LIMIT,
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        loadPostAsync(response.getRawResponse());
                    }
                }
        );
        graphRequest.executeAsync();
    }

    private void loadPostAsync(String response) {
        ProductLoader productLoader = new ProductLoader(response);
        productLoader.setResponseListener(new ProductLoader.ResponseListener() {
            @Override
            public void onResponse() {
                if (!AppConstants.PRODUCT_CACHE.isEmpty()) {
                    // load sell items
                    loadSellItems();
                    // load post items
                    loadPostItems();
                    // load popular posts
                    loadPopularItems();
                }
                hideLoader();
            }
        });
        productLoader.execute();
    }

    private void loadSellItems() {

        if (!sellItemList.isEmpty()) {
            sellItemList.clear();
        }
        for (ProductModel postItem : AppConstants.PRODUCT_CACHE) {

            if (postItem.getType().equalsIgnoreCase(AppConstants.POST_TYPE_COMMERCE)) {
                sellItemList.add(postItem);
            }
        }

        if (!sellItemList.isEmpty()) {
            sellItemParent.setVisibility(View.VISIBLE);
            tvSellListTitle.setText(getString(R.string.sell_item) + AppConstants.START_BRACE + sellItemList.size() + AppConstants.END_STRING);
            sellItemAdapter.setDisplayCount(AppConstants.HOME_ITEM_MAX);
            sellItemAdapter.notifyDataSetChanged();
        }
    }

    private void loadPostItems() {

        if (!postItemList.isEmpty()) {
            postItemList.clear();
        }
        for (ProductModel postItem : AppConstants.PRODUCT_CACHE) {
            if (postItem.getType().equalsIgnoreCase(AppConstants.POST_TYPE_PHOTO)
                    || postItem.getType().equalsIgnoreCase(AppConstants.POST_TYPE_ALBUM)) {
                postItemList.add(postItem);
            }
        }

        if (!postItemList.isEmpty()) {
            postItemParent.setVisibility(View.VISIBLE);
            tvPostItemTitle.setText(getString(R.string.post_item) + AppConstants.START_BRACE + postItemList.size() + AppConstants.END_STRING);   // set list title
            postItemAdapter.setDisplayCount(AppConstants.HOME_ITEM_MAX);           // set display item limit
            postItemAdapter.notifyDataSetChanged();
        }

    }

    private void loadPopularItems() {

        if (!popularItemList.isEmpty()) {
            popularItemList.clear();
        }
        for (ProductModel postItem : AppConstants.PRODUCT_CACHE) {
            if (postItem.getLikeCount() > AppConstants.MIN_LIKE_FOR_POPULAR) {
                popularItemList.add(postItem);
            }
        }

        if (!popularItemList.isEmpty()) {
            popularItemParent.setVisibility(View.VISIBLE);
            tvPopularListTitle.setText(getString(R.string.popular_item) + AppConstants.START_BRACE + popularItemList.size() + AppConstants.END_STRING);
            popularItemAdapter.setDisplayCount(AppConstants.HOME_ITEM_MAX);
            popularItemAdapter.notifyDataSetChanged();
        }
    }
}
