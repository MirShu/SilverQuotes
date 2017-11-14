package com.quantpower.silverquotes.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.ui.fragment.NavigationFragment;
import com.quantpower.silverquotes.utils.BackHandlerHelper;
import com.quantpower.silverquotes.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private NavigationFragment mNavigationFragment;
    private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.setDebugMode(true);
        setCurrentFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mNavigationFragment == null) {
            mNavigationFragment = NavigationFragment.newInstance(getString(R.string.navigation_navigation_bar));
        }
        transaction.replace(R.id.frame_content, mNavigationFragment);

    }

    private void setCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNavigationFragment = NavigationFragment.newInstance(getString(R.string.navigation_navigation_bar));
        transaction.replace(R.id.frame_content, mNavigationFragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
        }
        transaction.commit();
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public interface FragmentBackHandler {
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if ((System.currentTimeMillis() - mExitTime) < 2000) {
                super.onBackPressed();
            } else {
                mExitTime = System.currentTimeMillis();
                UIHelper.toastMessage(MainActivity.this, "再按一次退出");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);//友盟统计
    }
}
