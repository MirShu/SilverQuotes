package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.widget.LodingDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ShuLin on 2017/5/22.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class OriginalPageActivity extends Activity {

    public static final String ORIGINALLINK = "original_link";
    private WebView wbOriginalLink;
    private String original_link;
    private TextView tv_head_title;
    private ImageView iv_back_icon;
    private LodingDialog lodingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_page);
        this.original_link = getIntent().getStringExtra(ORIGINALLINK);
        this.wbOriginalLink = (WebView) findViewById(R.id.wb_original_link);
        this.tv_head_title = (TextView) findViewById(R.id.tv_main_title);
        this.iv_back_icon = (ImageView) findViewById(R.id.iv_back_icon);
        this.tv_head_title.setText("");
        this.iv_back_icon.setOnClickListener(view -> finish());
        this.wbOriginalLink.loadUrl(original_link);
        lodingDialog = new LodingDialog(this, "");
        lodingDialog.show();
        initData();
        fillView();
    }

    public void initData() {
        wbOriginalLink.getSettings().setJavaScriptEnabled(true);
        wbOriginalLink.getSettings().setSupportZoom(true);
        wbOriginalLink.getSettings().setBuiltInZoomControls(true);
        wbOriginalLink.getSettings().setUseWideViewPort(true);
        WebSettings webSettings = wbOriginalLink.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        wbOriginalLink.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wbOriginalLink.getSettings().setLoadWithOverviewMode(true);
    }

    public void fillView() {
        wbOriginalLink.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                lodingDialog.dismiss();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                tv_head_title.setText(view.getTitle());

            }
        });
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
