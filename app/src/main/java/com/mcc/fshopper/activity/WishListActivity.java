package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mcc.fshopper.R;
import com.mcc.fshopper.adapter.WishListAdapter;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.data.sqlite.WishDBController;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.WishItem;
import com.mcc.fshopper.utils.DialogUtils;

import java.util.ArrayList;

/**
 * Created by Nasir on 7/11/17.
 */

public class WishListActivity extends BaseActivity {

    // initialize variables
    private Context mContext;
    private Activity mActivity;

    private RecyclerView rvWishList;
    private ArrayList<WishItem> wishList;
    private WishListAdapter wishListAdapter;
    private static final int COLUMN_SPAN_COUNT = 2;

    private TextView info_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        initToolbar();
        loadWishData();
        initLister();
    }

    private void initVariables() {
        mContext = getApplicationContext();
        mActivity = WishListActivity.this;

        wishList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_wish_list);

        initToolbar();
        enableBackButton();
        initLoader();

        rvWishList = (RecyclerView) findViewById(R.id.rvWishList);

        // init RecyclerView
        rvWishList.setHasFixedSize(true);

        rvWishList.setLayoutManager(new LinearLayoutManager(mActivity));
        wishListAdapter = new WishListAdapter(mContext, wishList);
        rvWishList.setAdapter(wishListAdapter);
        info_text = (TextView) findViewById(R.id.info_text);
    }

    private void initLister() {
        wishListAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View viewItem, int position) {

                switch (viewItem.getId()) {
                    case R.id.ivRemoveWish:

                        // Remove from wish
                        deleteWishItemDialog(wishList.get(position).productId);
                        break;
                    default:

                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra(AppConstants.PRODUCT_ID, wishList.get(position).productId);
                        startActivity(intent);;

                        break;
                }

            }
        });

    }

    private void loadWishData() {
        if (!wishList.isEmpty()) {
            wishList.clear();
        }
        try {
            WishDBController wishController = new WishDBController(mContext);
            wishController.open();
            wishList.addAll(wishController.getAllWishData());
            wishController.close();

            wishListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (wishList.isEmpty()) {
            showEmptyView();
            info_text.setText(getString(R.string.empty_wish_list));
        } else {
            hideLoader();
        }


    }

    private void deleteWishItemDialog(final String productId) {

        DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_wish_item), getString(R.string.dialog_btn_yes), getString(R.string.dialog_btn_no), true, new DialogUtils.DialogActionListener() {
            @Override
            public void onPositiveClick() {
                try {
                    WishDBController wishController = new WishDBController(mContext);
                    wishController.open();
                    wishController.deleteWishItemById(productId);
                    wishController.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadWishData();
            }
        });

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

}
