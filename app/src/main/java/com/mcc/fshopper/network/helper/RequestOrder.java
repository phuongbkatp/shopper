package com.mcc.fshopper.network.helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.mcc.fshopper.R;
import com.mcc.fshopper.model.OrderModel;
import com.mcc.fshopper.utils.AppUtility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nasir on 4/07/17.
 */

public class RequestOrder extends AsyncTask<Void, Void, Void> {

    private ResponseListener responseListener;
    private Sheets mService = null;
    private Intent intent;
    private Object object;

    private Context mContext;

    private OrderModel orderModel;

    public RequestOrder(Context context, GoogleAccountCredential credential) {
        this.mContext = context;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("eShopper")
                .build();
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void setParams(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Column - Parameter
            //======================
            // A - Product Details
            // B - Order Notes
            // C - Name
            // D - Mobile
            // E - Email
            // F - FacebookId
            // G - Address
            // H - City
            // I - State
            // J - Postal Code
            // K - Country
            // L - Shipping method
            // M - Payment Method
            // N - Transection ID

            List<Object> orderInformations = Arrays.asList(
                    (Object) AppUtility.getCurrentTime(),
                    (Object) orderModel.getProductDetails(),
                    (Object) orderModel.getProductNote(),
                    (Object) orderModel.getUserModel().getName(),
                    (Object) orderModel.getUserModel().getMobile(),
                    (Object) orderModel.getUserModel().getEmail(),
                    (Object) orderModel.getUserModel().getFacebookId(),
                    (Object) orderModel.getUserModel().getAddress(),
                    (Object) orderModel.getUserModel().getCity(),
                    (Object) orderModel.getUserModel().getState(),
                    (Object) orderModel.getUserModel().getPostalCode(),
                    (Object) orderModel.getUserModel().getCountry(),
                    (Object) orderModel.getShippingMethod(),
                    (Object) orderModel.getPaymentMethod(),
                    (Object) orderModel.getTransactionId()
            );
            List<List<Object>> values = Arrays.asList(orderInformations);
            ValueRange body = new ValueRange().setValues(values);
            AppendValuesResponse result = mService.spreadsheets().
                    values().append(mContext.getResources().getString(R.string.order_sheet_id), "Sheet1!A:N", body).
                    setValueInputOption("RAW").execute();
            object = true;
        } catch (UserRecoverableAuthIOException e) {
            e.printStackTrace();
            intent = e.getIntent();
            object = intent;
        } catch (IOException e) {
            object = false;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void output) {
        if (responseListener != null) {
            responseListener.onResponse(object);
        }
    }

    public interface ResponseListener{
        public void onResponse(Object object);
    }

}
