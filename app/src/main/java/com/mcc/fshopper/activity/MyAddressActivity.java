package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mcc.fshopper.R;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.data.preference.AppPreference;
import com.mcc.fshopper.data.preference.PrefKey;
import com.mcc.fshopper.model.OrderModel;
import com.mcc.fshopper.model.UserModel;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AppUtility;


/**
 * Created by Nasir on 7/4/17.
 */

public class MyAddressActivity extends BaseActivity{

    private Context mContext;
    private Activity mActivity;

    // User info/ billing
    private EditText etName, etMobile, etEmail, etFacebookId, etAddress, etCity,
            etState, etPostalCode, etCountry;
    private TextView tvTotalPrice;

    // UI
    private Button btnNext;

    // edit customer info
    private OrderModel orderModel = null;
    private boolean editOnly = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        loadData();
        initListener();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = MyAddressActivity.this;
        //lineItems = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.KEY_ORDER_MODEL)) {
            orderModel = (OrderModel) intent.getSerializableExtra(AppConstants.KEY_ORDER_MODEL);
        }
        if (intent.hasExtra(AppConstants.KEY_EDIT_ONLY)) {
            editOnly = intent.getBooleanExtra(AppConstants.KEY_EDIT_ONLY, false);
        }

    }

    private void initView() {

        setContentView(R.layout.activity_my_address);

        initToolbar();
        enableBackButton();
        setToolbarTitle(getString(R.string.menu_my_address));

        btnNext = (Button) findViewById(R.id.btnNext);

        etName = (EditText) findViewById(R.id.et_name);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etEmail = (EditText) findViewById(R.id.et_email);
        etFacebookId = (EditText) findViewById(R.id.et_facebook_id);
        etAddress = (EditText) findViewById(R.id.et_address);
        etCity = (EditText) findViewById(R.id.et_city);
        etState = (EditText) findViewById(R.id.et_state);
        etPostalCode = (EditText) findViewById(R.id.et_postal_code);
        etCountry = (EditText) findViewById(R.id.et_country);

        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);

    }

    private void initListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidateInput()) {
                    storeUserData();
                    if (editOnly) {
                        AppUtility.showToast(mContext, getString(R.string.successfully_updated));
                        finish();
                    } else {
                        ActivityUtils.getInstance().invokePlaceOrder(mActivity, orderModel);
                    }

                } else {
                    AppUtility.showToast(mContext, getString(R.string.address_validation));
                }
            }
        });
    }



    // load ui data from preference
    private void loadData() {

        if (editOnly) {
            btnNext.setText(getResources().getString(R.string.update));
            tvTotalPrice.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setText(getResources().getString(R.string.next));
            //tvTotalPrice.setText("Total: " + AppConstants.CURRENCY + AppPreference.getInstance(mContext).getString(PrefKey.PAYMENT_TOTAL_PRICE));
        }

        String name = AppPreference.getInstance(mContext).getString(PrefKey.NAME);
        String mobile = AppPreference.getInstance(mContext).getString(PrefKey.MOBILE);
        String email = AppPreference.getInstance(mContext).getString(PrefKey.EMAIL);
        String address = AppPreference.getInstance(mContext).getString(PrefKey.ADDRESS);
        String facebookId = AppPreference.getInstance(mContext).getString(PrefKey.FACEBOOK_ID);
        String city = AppPreference.getInstance(mContext).getString(PrefKey.CITY);
        String state = AppPreference.getInstance(mContext).getString(PrefKey.STATE);
        String postalCode = AppPreference.getInstance(mContext).getString(PrefKey.POSTAL_CODE);
        String country = AppPreference.getInstance(mContext).getString(PrefKey.COUNTRY);

        etName.setText(name);
        etMobile.setText(mobile);
        etEmail.setText(email);
        etAddress.setText(address);
        etFacebookId.setText(facebookId);
        etCity.setText(city);
        etState.setText(state);
        etPostalCode.setText(postalCode);
        etCountry.setText(country);

        if(orderModel != null) {
            orderModel.setUserModel(new UserModel(name, mobile, email, facebookId, address, city, state, postalCode, country));
        }
    }

    private boolean isValidateInput() {
        return !etMobile.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty() && !etAddress.getText().toString().isEmpty();
    }

    private void storeUserData() {
        AppPreference.getInstance(mContext).setString(PrefKey.NAME, etName.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.MOBILE, etMobile.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.EMAIL, etEmail.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.ADDRESS, etAddress.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.FACEBOOK_ID, etFacebookId.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.CITY, etCity.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.STATE, etState.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.POSTAL_CODE, etPostalCode.getText().toString());
        AppPreference.getInstance(mContext).setString(PrefKey.COUNTRY, etCountry.getText().toString());
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
