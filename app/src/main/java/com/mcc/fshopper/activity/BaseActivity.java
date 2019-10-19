package com.mcc.fshopper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.R;
import com.mcc.fshopper.utils.ActivityUtils;
import com.mcc.fshopper.utils.AdUtils;
import com.mcc.fshopper.utils.AnalyticsUtils;
import com.mcc.fshopper.utils.AppUtility;
import com.mcc.fshopper.utils.PermissionUtils;

/**
 * Created by Nasir on 5/14/17.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private LinearLayout loadingView, noDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();

        // uncomment this line to disable ad
        // AdUtils.getInstance(mContext).disableBannerAd();
        // AdUtils.getInstance(mContext).disableInterstitialAd();

    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = BaseActivity.this;
    }

    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
        }
        return mToolbar;
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void initDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        //mToolbar.setNavigationIcon(R.drawable.ic_toggle_24);
        //mToolbar.setTitleTextColor(getResources().getColor(R.color.blue));

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_my_address) {

            // analytics event trigger
            AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("Drawer : My account");

            ActivityUtils.getInstance().invokeAddressActivity(mActivity, null, true, false);

        } else if (id == R.id.action_wish) {
            // analytics event trigger
            AnalyticsUtils.getAnalyticsUtils(mContext).trackEvent("Drawer : Wish list");

            ActivityUtils.getInstance().invokeActivity(mActivity, WishListActivity.class, false);
        }

        // support
        else if (id == R.id.action_call) {
            AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);
        } else if (id == R.id.action_message) {
            AppUtility.sendSMS(mActivity, AppConstants.SMS_NUMBER, AppConstants.SMS_TEXT);
        } else if (id == R.id.action_messenger) {
            AppUtility.invokeMessengerBot(mActivity, getResources().getString(R.string.facebook_page_id));
        } else if (id == R.id.action_email) {
            AppUtility.sendEmail(mActivity, AppConstants.EMAIL_ADDRESS, AppConstants.EMAIL_SUBJECT, AppConstants.EMAIL_BODY);
        }

        // others
        else if (id == R.id.action_share) {
            AppUtility.shareApp(mActivity);
        } else if (id == R.id.action_rate_app) {
            AppUtility.rateThisApp(mActivity); // this feature will only work after publish the app
        } else if (id == R.id.action_settings) {
            ActivityUtils.getInstance().invokeActivity(mActivity, SettingsActivity.class, false);
        } else if (id == R.id.terms_conditions) {
            ActivityUtils.getInstance().invokeWebPageActivity(mActivity, getResources().getString(R.string.terms), getResources().getString(R.string.terms_url));
        } else if (id == R.id.privacy_policy) {
            ActivityUtils.getInstance().invokeWebPageActivity(mActivity, getResources().getString(R.string.privacy), getResources().getString(R.string.privacy_url));
        } else if (id == R.id.faq) {
            ActivityUtils.getInstance().invokeWebPageActivity(mActivity, getResources().getString(R.string.faq), getResources().getString(R.string.faq_url));
        }

        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    public void initLoader() {
        loadingView = (LinearLayout) findViewById(R.id.loadingView);
        noDataView = (LinearLayout) findViewById(R.id.noDataView);
    }

    public void showLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }

        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void showEmptyView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionUtils.isPermissionResultGranted(grantResults)) {
             if (requestCode == PermissionUtils.REQUEST_CALL) {
                AppUtility.makePhoneCall(mActivity, AppConstants.CALL_NUMBER);
            }
        } else {
            AppUtility.showToast(mActivity, getString(R.string.permission_not_granted));
        }

    }

    public void showAdThenActivity(final Class<?> activity) {
        if (AdUtils.getInstance(mContext).showFullScreenAd()) {
            AdUtils.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    ActivityUtils.getInstance().invokeActivity(mActivity, activity, false);
                }
            });
        } else {
            ActivityUtils.getInstance().invokeActivity(mActivity, activity, false);
        }
    }


}
