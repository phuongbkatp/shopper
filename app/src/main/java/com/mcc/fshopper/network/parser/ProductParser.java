package com.mcc.fshopper.network.parser;

import android.os.AsyncTask;

import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.network.helper.RequestOrder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashiq on 2/11/2017.
 */
public class ProductParser {

    public ArrayList<ProductModel> getProductList(String string) {
        ArrayList<ProductModel> productList = new ArrayList<>();
        if (string != null) {
            try {

                JSONObject parentObject = new JSONObject(string);

                JSONArray jsonArray = parentObject.getJSONArray(ParserKey.KEY_DATA);


                for (int i = 0; i < jsonArray.length(); i++) {
                    ProductModel product = getProduct(jsonArray.get(i).toString());
                    if (product != null) {
                        productList.add(product);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return productList;
    }

    // parse and return single product
    public ProductModel getProduct(String responseItem) {

        try {
            JSONObject jsonObject = new JSONObject(responseItem);
            // variables

            int likeCount = 0;
            String id = null, title = null, price = null, details = null, location = null, message = null, link = null, type = null,
                     attachments = null, createDate = null, updateTime = null, sellerId = null;
            ArrayList<String> imageList = null;


            if (jsonObject.has(ParserKey.KEY_ID)) {
                id = jsonObject.getString(ParserKey.KEY_ID);
            }
            if (jsonObject.has(ParserKey.KEY_MESSAGE)) {

                message = jsonObject.getString(ParserKey.KEY_MESSAGE);
                // split title, price, location and description from message
                if (!message.isEmpty()) {
                    details = message;
                    try {
                        String lines[] = message.split("\\r?\\n");

                        title = lines[0];
                        if (lines.length > 0) {
                            String priceLocation[] = lines[1].split("-"); // split price location by '-'

                            String priceStr = priceLocation[0];
                            if(priceStr.matches(".*\\d+.*")) {
                                price = priceStr;
                            }

                            if (priceLocation.length > 0) {
                                location = priceLocation[1];
                            }
                        }


                    } catch (Exception e) {
                    }
                }
            }
            if (jsonObject.has(ParserKey.KEY_LINK)) {
                link = jsonObject.getString(ParserKey.KEY_LINK);
            }

            if (jsonObject.has(ParserKey.KEY_ATTACHMENTS)) {

                imageList = new ArrayList<>();

                attachments = jsonObject.getString(ParserKey.KEY_ATTACHMENTS);
                JSONObject objAttachment = new JSONObject(attachments);
                JSONArray arrayAttachmentData = objAttachment.getJSONArray(ParserKey.KEY_DATA);
                for (int j = 0; j < arrayAttachmentData.length(); j++) {
                    JSONObject objAttachmentData = arrayAttachmentData.getJSONObject(j);

                    if (objAttachmentData.has(ParserKey.KEY_TYPE)) {

                        if (objAttachmentData.has(ParserKey.KEY_TITLE)) {
                            title = objAttachmentData.getString(ParserKey.KEY_TITLE);
                        }

                        type = objAttachmentData.getString(ParserKey.KEY_TYPE);

                        if (type.equalsIgnoreCase(ParserKey.KEY_TYPE_PHOTO)
                                || type.equalsIgnoreCase(ParserKey.KEY_TYPE_ALBUM)
                                || type.equalsIgnoreCase(ParserKey.KEY_TYPE_COMMERCE)) {


                            if (objAttachmentData.has(ParserKey.KEY_SUB_ATTACHMENTS)) {
                                JSONObject objSubAttachment = objAttachmentData.getJSONObject(ParserKey.KEY_SUB_ATTACHMENTS);

                                JSONArray arraySubAttachmentData = objSubAttachment.getJSONArray(ParserKey.KEY_DATA);

                                for (int k = 0; k < arraySubAttachmentData.length(); k++) {

                                    JSONObject objSubAttachmentData = arraySubAttachmentData.getJSONObject(k);

                                    if (objSubAttachmentData.has("media")) {
                                        JSONObject objMedia = objSubAttachmentData.getJSONObject("media");
                                        JSONObject objImage = objMedia.getJSONObject("image");

                                        if (objImage.has(ParserKey.KEY_SRC)) {
                                            String imageUrl = objImage.getString(ParserKey.KEY_SRC);

                                            imageList.add(imageUrl);

                                        }
                                    }
                                }
                            } else {
                                if (objAttachmentData.has("media")) {
                                    JSONObject objMedia = objAttachmentData.getJSONObject("media");
                                    JSONObject objImage = objMedia.getJSONObject("image");

                                    if (objImage.has(ParserKey.KEY_SRC)) {
                                        String imageUrl = objImage.getString(ParserKey.KEY_SRC);

                                        imageList.add(imageUrl);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (jsonObject.has(ParserKey.KEY_LIKES)) {
                JSONObject likeObj = jsonObject.getJSONObject(ParserKey.KEY_LIKES);
                if (likeObj.has(ParserKey.KEY_LIKE_SUMMARY)) {
                    JSONObject summaryObj = likeObj.getJSONObject(ParserKey.KEY_LIKE_SUMMARY);

                    if (summaryObj.has(ParserKey.KEY_LIKE_COUNT)) {
                        likeCount = summaryObj.getInt(ParserKey.KEY_LIKE_COUNT);
                    }
                }
            }

            if (jsonObject.has(ParserKey.KEY_CREATED_TIME)){
                createDate = jsonObject.getString(ParserKey.KEY_CREATED_TIME);
            }
            if (jsonObject.has(ParserKey.KEY_UPDATED_TIME)){
                updateTime = jsonObject.getString(ParserKey.KEY_UPDATED_TIME);
            }
            if (jsonObject.has(ParserKey.KEY_FROM)){

                sellerId = jsonObject.getJSONObject(ParserKey.KEY_FROM).getString(ParserKey.KEY_ID);
            }

            if (imageList !=null && !imageList.isEmpty()) {
                return new ProductModel(id, title, price, details, location, imageList, message, link, type, likeCount, createDate, updateTime, sellerId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
