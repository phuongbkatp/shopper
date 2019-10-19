package com.mcc.fshopper.data.constant;

import com.mcc.fshopper.model.ProductModel;

import java.util.ArrayList;

/**
 * Created by Nasir on 5/24/17.
 */

public class AppConstants {
    // Integer constants
    public static final int VALUE_ZERO = 0;
    public static final int INDEX_ZERO = 0;
    public static final String EMPTY_STRING = "";
    public static final String START_BRACE = " (";
    public static final String END_STRING = ")";

    // change this count to adjust your popular list
    public static final int MIN_LIKE_FOR_POPULAR = 0;

    // post limit per call
    public static final int POST_LIMIT = 100; // max limit for page is 100, and no max limit for group

    // ISO 8601 date time format
    public static final String DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss";

    // search page key
    public static final int INITIAL_PAGE_NUMBER = 1;
    public static final String SEARCH_KEY = "searchKey";

    // String constants
    public static final String PRODUCT_ID = "productId";
    public static final String CATEGORY_ID = "categoryId";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String PAGE_TYPE = "pageType";
    public static final String ORDER_ID = "orderId";
    public static final String SELLER_ID = "sellerId";

    public static final String KEY_ORDER_MODEL = "order_model";
    public static final String KEY_EDIT_ONLY = "editOnly";

    // Large image view
    public static final String KEY_IMAGE_URL = "large_image_url";

    // order list
    public static final String[] orderTitles = {"Order by title", "Order by date"};
    public static final int ORDER_POSITION_TITLE = 0;
    public static final int ORDER_POSITION_DATE = 1;
    // Notification keys
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_MESSAGE = "message";
    public static final String BUNDLE_KEY_URL = "url";

    // Notification type
    public static final String NOTIFY_TYPE_MESSAGE = "message";
    public static final String NOTIFY_TYPE_PRODUCT = "product";
    public static final String NOTIFY_TYPE_URL     = "webpage";

    public static final String CALL_NUMBER = "+123456789"; // replace by your call support  number

    public static final String SMS_NUMBER = "+123456789"; // replace by your support sms number
    public static final String SMS_TEXT = "Send your feedback to improve our service..."; // replace by your message
    public static final String CONTACT_TEXT = "Your message..."; // replace by your message

    public static final String EMAIL_ADDRESS = "products.mcc@gmail.com"; // replace by your support sms number
    public static final String EMAIL_SUBJECT = "Feedback"; // replace by your message
    public static final String EMAIL_BODY = "Send your feedback to improve our service..."; // replace by your message

    public static final int HOME_ITEM_MAX = 4;

    // static fb data list
    public static final ArrayList<ProductModel> PRODUCT_CACHE = new ArrayList<>();


    // fb Post type
    public static final String POST_TYPE_PHOTO = "photo";
    public static final String POST_TYPE_ALBUM = "album";
    public static final String POST_TYPE_COMMERCE = "commerce_product_item";

    // List type
    public static final String LIST_TYPE = "list_type";
    public static final String LIST_TYPE_SELL = "list_sell";
    public static final String LIST_TYPE_POST = "list_post";
    public static final String LIST_TYPE_POPULAR = "list_popular";

    // Comment list
    public static final String COMMENT_LIMIT_VALUE = "3";

}
