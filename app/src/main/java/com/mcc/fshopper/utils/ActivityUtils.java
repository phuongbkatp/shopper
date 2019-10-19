package com.mcc.fshopper.utils;

import android.app.Activity;
import android.content.Intent;

import com.mcc.fshopper.activity.ProductDetailsActivity;
import com.mcc.fshopper.activity.ProductListActivity;
import com.mcc.fshopper.activity.SearchListActivity;
import com.mcc.fshopper.activity.LargeImageViewActivity;
import com.mcc.fshopper.activity.MyAddressActivity;
import com.mcc.fshopper.activity.NotificationContentActivity;
import com.mcc.fshopper.activity.OrderConfirmationPage;
import com.mcc.fshopper.activity.PlaceOrderActivity;
import com.mcc.fshopper.activity.WebPageActivity;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.model.OrderModel;

/**
 * Created by Ashiq on 5/11/16.
 */
public class ActivityUtils {

    private static ActivityUtils sActivityUtils = null;

    public static ActivityUtils getInstance() {
        if (sActivityUtils == null) {
            sActivityUtils = new ActivityUtils();
        }
        return sActivityUtils;
    }

    public void invokeActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeFbProducts(Activity activity, String pageTitle, String postType) {
        Intent intent = new Intent(activity, ProductListActivity.class);
        intent.putExtra(AppConstants.PAGE_TITLE, pageTitle);
        intent.putExtra(AppConstants.LIST_TYPE, postType);

        activity.startActivity(intent);
    }

    public void invokeProductDetails(Activity mActivity, String productId, String sellerId){
        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
        intent.putExtra(AppConstants.PRODUCT_ID, productId);
        intent.putExtra(AppConstants.SELLER_ID, sellerId);
        mActivity.startActivity(intent);
    }

    public void invokeImage(Activity activity, String imageUrl) {
        Intent intent = new Intent(activity, LargeImageViewActivity.class);
        intent.putExtra(AppConstants.KEY_IMAGE_URL, imageUrl);
        activity.startActivity(intent);
    }

    public void invokeAddressActivity(Activity activity, OrderModel orderModel, boolean editOnly, boolean shouldFinish) {
        Intent intent = new Intent(activity, MyAddressActivity.class);
        intent.putExtra(AppConstants.KEY_ORDER_MODEL, orderModel);
        intent.putExtra(AppConstants.KEY_EDIT_ONLY, editOnly);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }


    public void invokeWebPageActivity(Activity activity, String pageTitle, String url) {
        Intent intent = new Intent(activity, WebPageActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_TITLE, pageTitle);
        intent.putExtra(AppConstants.BUNDLE_KEY_URL, url);
        activity.startActivity(intent);
    }

    public void invokeNotifyContentActivity(Activity activity, String title, String message) {
        Intent intent = new Intent(activity, NotificationContentActivity.class);
        intent.putExtra(AppConstants.BUNDLE_KEY_TITLE, title);
        intent.putExtra(AppConstants.BUNDLE_KEY_MESSAGE, message);
        activity.startActivity(intent);
    }

    public void invokeFbSearchListActivity(Activity activity, String searchKey) {
        Intent intent = new Intent(activity, SearchListActivity.class);
        intent.putExtra(AppConstants.SEARCH_KEY, searchKey);
        activity.startActivity(intent);
    }


    public void invokeOrder(Activity activity) {
        Intent intent = new Intent(activity, OrderConfirmationPage.class);
        activity.startActivity(intent);
        activity.finish();
    }


    public void invokePlaceOrder(Activity activity, OrderModel orderModel) {
        Intent intentOrder = new Intent(activity, PlaceOrderActivity.class);
        intentOrder.putExtra(AppConstants.KEY_ORDER_MODEL, orderModel);
        activity.startActivity(intentOrder);
        activity.finish();
    }


}
