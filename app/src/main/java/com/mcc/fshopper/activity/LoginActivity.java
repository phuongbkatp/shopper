package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.facebook.login.widget.LoginButton;
import com.mcc.fshopper.R;
import com.mcc.fshopper.data.preference.AppPreference;
import com.mcc.fshopper.data.preference.PrefKey;
import com.mcc.fshopper.registration.BaseLoginActivity;
import com.mcc.fshopper.registration.LoginListener;
import com.mcc.fshopper.registration.LoginModel;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AnalyticsUtils;
import com.mcc.fshopper.utils.AppUtility;

/**
 * Created by Nasir on 6/15/17.
 */

public class LoginActivity extends BaseLoginActivity {

    // initialize variables
    private Context mContext;
    private Activity mActivity;

    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFbLogin(loginButton);
        initListener();
    }

    private void initVar() {
        // variables instance
        mContext = getApplicationContext();
        mActivity = LoginActivity.this;

        // analytics event trigger
        AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("Login Activity");
    }

    private void initView() {
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.btnLoginFacebook);
    }

    private void initListener() {


        setLoginListener(new LoginListener() {
            @Override
            public void onLoginResponse(LoginModel loginModel) {
                if (loginModel.getUserId() != null) {

                    AppPreference.getInstance(mContext).setBoolean(PrefKey.REGISTERED, true);
                    AppPreference.getInstance(mContext).setString(PrefKey.NAME, loginModel.getName());
                    AppPreference.getInstance(mContext).setString(PrefKey.EMAIL, loginModel.getEmail());

                    ActivityUtils.getInstance().invokeActivity(mActivity, MainActivity.class, true);
                } else {
                    AppUtility.showToast(mContext, getString(R.string.failed));
                }
            }
        });
    }



}
