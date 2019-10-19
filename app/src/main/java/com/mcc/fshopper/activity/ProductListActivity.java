package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mcc.fshopper.R;
import com.mcc.fshopper.adapter.ProductListAdapter;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.ListTypeShow;

import java.util.ArrayList;

/**
 * Created by Nasir on 5/15/17.
 */

public class ProductListActivity extends BaseActivity {

    // initialize variables
    private Activity mActivity;
    private Context mContext;

    private RecyclerView rvProductList;
    private ArrayList<ProductModel> productList;
    private ProductListAdapter mProductListAdapter;

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private static final int COLUMN_SPAN_COUNT = 2;
    private ProgressBar loadMoreView;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    // initialize View
    private ImageView viewToggle;

    private String title, dataType;

    private int pageNumber = AppConstants.INITIAL_PAGE_NUMBER;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        loadProductList();
        initListener();
    }

    private void initVariable() {
        mActivity = ProductListActivity.this;
        mContext = mActivity.getApplicationContext();

        productList = new ArrayList<>();

        Intent intent = getIntent();
        title = intent.getStringExtra(AppConstants.PAGE_TITLE);
        dataType = intent.getStringExtra(AppConstants.LIST_TYPE);

    }

    private void initView() {
        setContentView(R.layout.activity_product_list);

        initToolbar();
        enableBackButton();
        setToolbarTitle(title);
        initLoader();

        rvProductList = (RecyclerView) findViewById(R.id.rvProductList);
        viewToggle = (ImageView) findViewById(R.id.viewToggle);
        loadMoreView = (ProgressBar) findViewById(R.id.loadMore);

        // init RecyclerView
        rvProductList.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rvProductList, LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        mProductListAdapter = new ProductListAdapter(mContext, productList, ListTypeShow.LINEAR);
        rvProductList.setAdapter(mProductListAdapter);

    }


    private void loadProductList() {

        if (dataType.equals(AppConstants.LIST_TYPE_SELL)){
            if (!productList.isEmpty()) {
                productList.clear();
            }

            for (int i = 0; i < AppConstants.PRODUCT_CACHE.size(); i++) {

                if (AppConstants.PRODUCT_CACHE.get(i).getType().equalsIgnoreCase(AppConstants.POST_TYPE_COMMERCE)) {
                    productList.add(AppConstants.PRODUCT_CACHE.get(i));
                }
            }
        }
        else if (dataType.equals(AppConstants.LIST_TYPE_POST)){
            if (!productList.isEmpty()) {
                productList.clear();
            }

            for (int i = 0; i < AppConstants.PRODUCT_CACHE.size(); i++) {
                if (AppConstants.PRODUCT_CACHE.get(i).getType().equalsIgnoreCase(AppConstants.POST_TYPE_PHOTO) || AppConstants.PRODUCT_CACHE.get(i).getType().equalsIgnoreCase(AppConstants.POST_TYPE_ALBUM)) {
                    productList.add(AppConstants.PRODUCT_CACHE.get(i));
                }
            }
        }
        else if (dataType.equals(AppConstants.LIST_TYPE_POPULAR)){
            if (!productList.isEmpty()) {
                productList.clear();
            }
            for (int i = 0; i < AppConstants.PRODUCT_CACHE.size(); i++) {
                if (AppConstants.PRODUCT_CACHE.get(i).getLikeCount() > 0) {
                    productList.add(AppConstants.PRODUCT_CACHE.get(i));
                }
            }
        }
        else {
            // All products
            if (!productList.isEmpty()) {
                productList.clear();
            }
            productList.addAll(AppConstants.PRODUCT_CACHE);
        }


        mProductListAdapter.notifyDataSetChanged();

        hideLoader();
    }

    private void initListener() {
        mProductListAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                // invoke product details activity by product id

                ProductModel post = productList.get(position);

                ActivityUtils.getInstance().invokeProductDetails(mActivity, post.getId(), post.getSellerId());
            }
        });


        viewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView();
            }
        });


        /*rvProductList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    onScrolledMore();
                }
            }
        });*/

    }

    // Generate list and grid layout manager
    public void setRecyclerViewLayoutManager(RecyclerView mRecyclerView, LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                gridLayoutManager = new GridLayoutManager(mActivity, COLUMN_SPAN_COUNT);
                mLayoutManager = gridLayoutManager;
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                linearLayoutManager = new LinearLayoutManager(mActivity);
                mLayoutManager = linearLayoutManager;
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

                break;
            default:
                linearLayoutManager = new LinearLayoutManager(mActivity);
                mLayoutManager = linearLayoutManager;
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
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

    private void toggleView() {
        if (mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
            viewToggle.setImageResource(R.drawable.ic_list);
            setRecyclerViewLayoutManager(rvProductList, LayoutManagerType.GRID_LAYOUT_MANAGER);

            mProductListAdapter = new ProductListAdapter(mContext, productList, ListTypeShow.GRID);
            rvProductList.setAdapter(mProductListAdapter);

        } else {
            viewToggle.setImageResource(R.drawable.ic_grid);
            setRecyclerViewLayoutManager(rvProductList, LayoutManagerType.LINEAR_LAYOUT_MANAGER);

            mProductListAdapter = new ProductListAdapter(mContext, productList, ListTypeShow.LINEAR);
            rvProductList.setAdapter(mProductListAdapter);
        }
    }
}
