package com.mcc.fshopper.activity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.R;
import com.mcc.fshopper.data.preference.AppPreference;
import com.mcc.fshopper.data.preference.PrefKey;
import com.mcc.fshopper.model.OrderModel;
import com.mcc.fshopper.network.helper.RequestOrder;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AppUtility;
import com.mcc.fshopper.utils.PermissionUtils;

import java.util.Arrays;

/**
 * Created by Nasir on 7/4/17.
 */

public class PlaceOrderActivity extends BaseActivity {

    // Variables
    private Context mContext;
    private Activity mActivity;

    // UI
    private TextView tvTotalPrice;
    private Button btnPlaceOrder;
    private RadioGroup rgPaymentMethod, rgShippingMethod;
    private EditText transactionIDInput;

    private String paymentMethod, shippingMethod;
    private String transactionID = AppConstants.EMPTY_STRING;

    private GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    static final int REQUEST_STRIPE = 999;

    private OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        initGoogleApi();
        initToolbar();
        initListener();

    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = PlaceOrderActivity.this;

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.KEY_ORDER_MODEL)) {
            orderModel = (OrderModel) intent.getSerializableExtra(AppConstants.KEY_ORDER_MODEL);
        }

        paymentMethod = getString(R.string.title_cash_on_delivery);
        shippingMethod = getString(R.string.home_delivery);
    }

    private void initView() {
        setContentView(R.layout.activity_place_order);

        initToolbar();
        setToolbarTitle(getString(R.string.finalize_order));
        enableBackButton();

        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        rgPaymentMethod = (RadioGroup) findViewById(R.id.rgPaymentMethod);
        rgShippingMethod = (RadioGroup) findViewById(R.id.rgShippingMethod);
        transactionIDInput = (EditText) findViewById(R.id.transactionID);

    }


    private void initListener() {
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdBank:
                        paymentMethod = getString(R.string.title_bank);
                        transactionIDInput.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rdCheck:
                        paymentMethod = getString(R.string.title_check);
                        transactionIDInput.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rdCashOnDelivery:
                        paymentMethod = getString(R.string.title_cash_on_delivery);
                        transactionIDInput.setVisibility(View.GONE);
                        break;
                    case R.id.rdStripe:
                        paymentMethod = getString(R.string.title_stripe);
                        transactionIDInput.setVisibility(View.GONE);
                        startActivityForResult(new Intent(mActivity, StripeActivity.class), REQUEST_STRIPE);
                        break;
                }
            }
        });

        rgShippingMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdOffice:
                        shippingMethod = getString(R.string.office_delivery);
                        break;
                    case R.id.rdHome:
                        shippingMethod = getString(R.string.home_delivery);
                        break;
                }
            }
        });
    }


    private void placeOrder() {

            getResultsFromApi();
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

    private void initGoogleApi() {
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SheetsScopes.SPREADSHEETS))
                .setBackOff(new ExponentialBackOff());
    }

    private void getResultsFromApi() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mActivity);
        if (connectionStatusCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
                apiAvailability.getErrorDialog(mActivity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES).show();
            }
        } else if (mCredential.getSelectedAccountName() == null) {
            if (PermissionUtils.isPermissionGranted(mActivity, PermissionUtils.ACCOUNT_PERMISSIONS, PermissionUtils.REQUEST_ACCOUNT)) {
                String accountName = AppPreference.getInstance(mContext).getString(PrefKey.ORDER_ACCOUNT_NAME);
                if (accountName != null && !accountName.isEmpty()) {
                    mCredential.setSelectedAccountName(accountName);
                    getResultsFromApi();
                } else {
                    startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                }
            }
        } else {
            executeOrder();
        }
    }


    private void executeOrder() {

        if(transactionID.isEmpty()) {
            transactionID = transactionIDInput.getText().toString();
        }

        orderModel.setPaymentMethod(paymentMethod);
        orderModel.setShippingMethod(shippingMethod);
        orderModel.setTransactionId(transactionID);

        RequestOrder requestOrder = new RequestOrder(mContext, mCredential);
        requestOrder.setParams(orderModel);
        requestOrder.setResponseListener(new RequestOrder.ResponseListener() {
            @Override
            public void onResponse(Object data) {
                if(data != null) {
                    if (data instanceof Intent) {
                        Intent intent = (Intent) data;
                        startActivityForResult(intent, REQUEST_AUTHORIZATION);
                    } else {
                        boolean success = (boolean) data;
                        if (success) {
                            ActivityUtils.getInstance().invokeOrder(mActivity);
                        } else {
                            AppUtility.showToast(mContext, getString(R.string.failed));
                        }
                    }
                } else {
                    AppUtility.showToast(mContext, getString(R.string.failed));
                }
            }
        });
        requestOrder.execute();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                transactionIDInput.setText(data.getStringExtra("stripe_token"));
            } else if (requestCode == REQUEST_GOOGLE_PLAY_SERVICES) {
                getResultsFromApi();
            } else if (requestCode == REQUEST_ACCOUNT_PICKER) {
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        AppPreference.getInstance(mContext).setString(PrefKey.ORDER_ACCOUNT_NAME, accountName);
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
            } else if (requestCode == REQUEST_AUTHORIZATION) {
                getResultsFromApi();
            } else if (requestCode == Activity.RESULT_CANCELED) {
                AppUtility.showToast(mContext, getString(R.string.cancelled));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtils.isPermissionResultGranted(grantResults)) {
            if (requestCode == PermissionUtils.REQUEST_ACCOUNT) {
                getResultsFromApi();
            }
        } else {
            AppUtility.showToast(mActivity, getString(R.string.permission_not_granted));
        }

    }

}
