package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.R;
import com.mcc.fshopper.utils.AdUtils;

/**
 * Created by Nasir on 7/5/17.
 */

public class OrderConfirmationPage extends BaseActivity {

    private Context mContext;
    private Activity mActivity;

    // UI
    private Button btnClose;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        loadData();
        initToolbar();
        initListener();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = OrderConfirmationPage.this;
    }

    private void initView() {
        setContentView(R.layout.activity_order_confirm);

        initToolbar();
        enableBackButton();
        enableBackButton();
        setToolbarTitle(getString(R.string.order_successful));

        btnClose = (Button) findViewById(R.id.btnClose);
    }


    private void loadData() {

        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.ORDER_ID)) {
            orderId = intent.getStringExtra(AppConstants.ORDER_ID);
        }
    }

    private void initListener() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // load full screen ad
        AdUtils.getInstance(mContext).loadFullScreenAd(mActivity);
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
