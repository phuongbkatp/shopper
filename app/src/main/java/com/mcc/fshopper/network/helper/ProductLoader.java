package com.mcc.fshopper.network.helper;

import android.os.AsyncTask;

import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.network.parser.ParserKey;
import com.mcc.fshopper.network.parser.ProductParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashiq on 2/11/2017.
 */
public class ProductLoader extends AsyncTask<Void, Void, Void>{

    private ResponseListener responseListener;
    private String response;

    public ProductLoader(String response) {
        this.response = response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!AppConstants.PRODUCT_CACHE.isEmpty()) {
            AppConstants.PRODUCT_CACHE.clear();
        }
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppConstants.PRODUCT_CACHE.addAll(new ProductParser().getProductList(response));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (responseListener != null) {
            responseListener.onResponse();
        }
    }

    public interface ResponseListener{
        public void onResponse();
    }


}
