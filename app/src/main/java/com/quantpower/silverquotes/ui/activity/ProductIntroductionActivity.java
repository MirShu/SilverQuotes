package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.quantpower.silverquotes.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ShuLin on 2017/5/17.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class ProductIntroductionActivity extends Activity {
    public static final String ORDER_TAG = "id_tag";
    private int orderTag;
    private String the_hang_seng_index_url = "file:///android_asset/the_hang_seng_index.jpg";
    private String crude_oil_futures = "file:///android_asset/crude_oil_futures.jpg";   //原油期货
    private String gold = "file:///android_asset/gold.jpg";   //黄金
    private String silver_futures = "file:///android_asset/silver_futures.jpg";
    private WebView wb_invite_process;
    private WebSettings settings;
    ImageView imageBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_introduction);
        imageBack = (ImageView) findViewById(R.id.image_back);
        imageBack.setOnClickListener(v -> finish());
        this.orderTag = getIntent().getIntExtra(ORDER_TAG, 0);
        this.wb_invite_process = (WebView) findViewById(R.id.wb_invite_process);

        if (orderTag == 0) {
            this.wb_invite_process.loadUrl(the_hang_seng_index_url);
        } else if (orderTag == 1) {
            this.wb_invite_process.loadUrl(crude_oil_futures);
        } else if (orderTag == 2) {
            this.wb_invite_process.loadUrl(gold);
        } else if (orderTag == 3) {
            this.wb_invite_process.loadUrl(silver_futures);
        }
        this.settings = wb_invite_process.getSettings();
        this.settings.setUseWideViewPort(true);
        this.settings.setLoadWithOverviewMode(true);
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
