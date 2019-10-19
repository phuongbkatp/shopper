package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcc.fshopper.R;
import com.mcc.fshopper.adapter.SearchListAdapter;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.listener.ListDialogActionListener;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AppUtility;
import com.mcc.fshopper.utils.ListTypeShow;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Nasir on 5/15/17.
 */

public class SearchListActivity extends BaseActivity {

    // initialize variables
    private Activity mActivity;
    private Context mContext;

    private RecyclerView rvProductList;
    private ArrayList<ProductModel> productList;
    private ArrayList<ProductModel> backupDataList;
    private SearchListAdapter mSearchListAdapter;

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout lytSortingView;
    private LinearLayout lytSorting;
    private static final int COLUMN_SPAN_COUNT = 2;
    private ProgressBar loadMoreView;
    private SearchView searchView;
    private TextView tvSortingTag;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    // initialize View
    private ImageView viewToggle;

    private String title;
    private int type, categoryId;

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
        mActivity = SearchListActivity.this;
        mContext = mActivity.getApplicationContext();

        productList = new ArrayList<>();
        backupDataList = new ArrayList<>();

        Intent intent = getIntent();
        title = intent.getStringExtra(AppConstants.PAGE_TITLE);
        type = intent.getIntExtra(AppConstants.PAGE_TYPE, AppConstants.VALUE_ZERO);
        categoryId = intent.getIntExtra(AppConstants.CATEGORY_ID, AppConstants.VALUE_ZERO);

    }

    private void initView() {
        setContentView(R.layout.activity_search_list);

        initToolbar();
        enableBackButton();
        setToolbarTitle(title);
        initLoader();

        rvProductList = (RecyclerView) findViewById(R.id.rvProductList);
        viewToggle = (ImageView) findViewById(R.id.ivListTypeIcon);
        loadMoreView = (ProgressBar) findViewById(R.id.loadMore);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint(getString(R.string.type_something));
        lytSortingView = (RelativeLayout) findViewById(R.id.lyt_sorting_view);
        lytSorting = (LinearLayout) findViewById(R.id.lyt_sorting);
        tvSortingTag = (TextView) findViewById(R.id.tvSortingTag);

        // init RecyclerView
        rvProductList.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rvProductList, LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        mSearchListAdapter = new SearchListAdapter(mContext, productList, ListTypeShow.LINEAR);
        rvProductList.setAdapter(mSearchListAdapter);

    }


    private void loadProductList() {

        productList.addAll(AppConstants.PRODUCT_CACHE);
        backupDataList.addAll(productList);

        mSearchListAdapter.notifyDataSetChanged();

        // set search key at search view that getting from main activity search view
        Intent intent = this.getIntent();
        String searchKey = intent.getStringExtra(AppConstants.SEARCH_KEY);

        if (productList.size()>0 && searchKey != null){
            searchView.setQuery(searchKey, false);
            performSearch(searchKey);
        }

        hideLoader();
    }

    private void initListener() {

        lytSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtility.showSortingAttributesDialog(mActivity, getResources().getString(R.string.select_order), AppConstants.orderTitles, new ListDialogActionListener() {
                    @Override
                    public void onItemSelected(int position) {

                        if (position == AppConstants.ORDER_POSITION_TITLE) {
                            // sort by title
                            Collections.sort(productList, new Comparator<ProductModel>() {
                                public int compare(ProductModel s1, ProductModel s2) {
                                    return s1.getTitle().compareToIgnoreCase(s2.getTitle());
                                }
                            });
                        }
                        else if (position == AppConstants.ORDER_POSITION_DATE) {
                            // sort by date
                            Collections.sort(productList, new Comparator<ProductModel>() {
                                public int compare(ProductModel s1, ProductModel s2) {
                                    return s1.getUpdateDate().compareToIgnoreCase(s2.getUpdateDate());
                                }
                            });
                            Collections.reverse(productList);
                        }
                        else {
                        }
                        tvSortingTag.setText(AppConstants.orderTitles[position]);
                        mSearchListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        mSearchListAdapter.setItemClickListener(new OnItemClickListener() {
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


/*        rvProductList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    onScrolledMore();
                }
            }
        });*/


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

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

            mSearchListAdapter = new SearchListAdapter(mContext, productList, ListTypeShow.GRID);
            rvProductList.setAdapter(mSearchListAdapter);

        } else {
            viewToggle.setImageResource(R.drawable.ic_grid);
            setRecyclerViewLayoutManager(rvProductList, LayoutManagerType.LINEAR_LAYOUT_MANAGER);

            mSearchListAdapter = new SearchListAdapter(mContext, productList, ListTypeShow.LINEAR);
            rvProductList.setAdapter(mSearchListAdapter);
        }
    }


    private ArrayList<ProductModel> filter(ArrayList<ProductModel> models, String searchKey) {
        searchKey = searchKey.toLowerCase();

        final ArrayList<ProductModel> filteredModelList = new ArrayList<>();
        for (ProductModel post : models) {
            try {
                String title = post.getTitle().toLowerCase();
                String message = post.getMessage().toLowerCase();

                if (title.contains(searchKey) || message.contains(searchKey)) {
                    filteredModelList.add(post);
                }
            } catch (Exception e) {
            }

        }
        return filteredModelList;
    }

    private void performSearch(String searchKey){
        if (!searchKey.isEmpty()) {
            productList.clear();
            productList.addAll(filter(backupDataList, searchKey));
            mSearchListAdapter.notifyDataSetChanged();
        }
        else {
            productList.clear();
            productList.addAll(backupDataList);
            mSearchListAdapter.notifyDataSetChanged();
        }
    }

}
