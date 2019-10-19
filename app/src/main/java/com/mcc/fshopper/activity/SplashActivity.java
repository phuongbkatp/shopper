package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mcc.fshopper.R;
import com.mcc.fshopper.data.preference.AppPreference;
import com.mcc.fshopper.data.preference.PrefKey;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AppUtility;

/**
 * Created by Nasir on 5/28/17.
 */

public class SplashActivity extends AppCompatActivity {

    // init variables
    private Context mContext;
    private Activity mActivity;
    private static final int SPLASH_DURATION = 200;

    // init view
    private LinearLayout lytGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initVariables();
        initView();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }

    private void initFunctionality() {

        lytGetStarted.setVisibility(View.VISIBLE);

        if (AppUtility.isNetworkAvailable(mContext)) {
            boolean registered = AppPreference.getInstance(mContext).getBoolean(PrefKey.REGISTERED);
            boolean skipped = AppPreference.getInstance(mContext).getBoolean(PrefKey.SKIPPED);

            if (registered || skipped) {
                lytGetStarted.setVisibility(View.INVISIBLE);
                lytGetStarted.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtils.getInstance().invokeActivity(mActivity, MainActivity.class, true);
                    }
                }, SPLASH_DURATION);
            }
        } else {
            lytGetStarted.setVisibility(View.INVISIBLE);
        }

        AppUtility.noInternetWarning(lytGetStarted, mContext);

    }

    private void initVariables() {
        mActivity = SplashActivity.this;
        mContext = mActivity.getApplicationContext();
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        lytGetStarted = (LinearLayout) findViewById(R.id.lytGetStarted);
    }

    private void initListener() {
        lytGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().invokeActivity(mActivity, LoginActivity.class, true);
            }
        });
    }

}