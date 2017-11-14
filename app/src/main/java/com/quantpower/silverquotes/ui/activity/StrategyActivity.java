package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.widget.CustomDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ShuLin on 2017/5/20.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class StrategyActivity extends Activity {
    String url = "http://3g.xftz.cn/ygy.html";
    private WebView marketWebView;
    private CustomDialog lodingDialog;
    private WebSettings wvSettings;
    private ImageView iv_back_icon;
//    private String menu_url = "http://m.dyhjw.com/quote/,http://m.dyhjw.com/shanghaihuangjin/,\r\nhttp://m.dyhjw.com/guojijin.html,http://m.dyhjw.com/zhipan.html,\r\nhttp://m.dyhjw.com/guzhi.html,http://m.dyhjw.com/guijinshu.html,\r\nhttp://m.dyhjw.com/meiguoyuanyou/";
    private String webview = "http://3g.xftz.cn/ygy.html";
    private String jquery = "$('.fr').remove();";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy);
        this.marketWebView = (WebView) findViewById(R.id.ww_strategy);
        this.iv_back_icon = (ImageView) findViewById(R.id.image_back);
//        this.iv_back_icon.setOnClickListener(v -> finish());
        iv_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marketWebView.canGoBack()){
                    marketWebView.goBack();
                }else {
                    finish();
                }
            }
        });
        loadJsCod();
        showWebViewData();
    }

    private void loadJsCod() {
        marketWebView.loadUrl(webview);
        jquery = "javascript: " + jquery;
        new Handler().postDelayed(() -> lodingDialog.dismiss(), 1500);

    }

    /**
     * 显示行情与隐藏行情上面title标题
     */
    private void showWebViewData() {
        this.marketWebView.setVisibility(View.INVISIBLE);
        lodingDialog = new CustomDialog(StrategyActivity.this, "加载中...");
        lodingDialog.show();
        wvSettings = marketWebView.getSettings();
        wvSettings.setJavaScriptEnabled(true);
        wvSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        wvSettings.setAppCacheEnabled(true);
        wvSettings.setDomStorageEnabled(true);
        wvSettings.setDatabaseEnabled(true);
        wvSettings.setAllowFileAccess(true);
        wvSettings.setBlockNetworkImage(true);
        wvSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        wvSettings.setDomStorageEnabled(true);
        marketWebView.setOnLongClickListener(view -> true);

        /**
         * 定时执行
         */
        new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        /**
         * WebView 控件webViewClient 所有回调方法
         */
        marketWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                if (menu_url.indexOf(url) != -1) {
//                } else {
//                }
                marketWebView.setVisibility(View.GONE);
                lodingDialog.show();

            }


            /**
             * 加载给定URL资源内容   该方法待使用    判断webview是否可以返回,不能返回就隐藏返回按钮
             */
            @Override
            public void onLoadResource(final WebView view, String url) {
                view.loadUrl(jquery);
                super.onLoadResource(view, url);
            }

            /**
             * 页面加载完成回调方法，获取对应url地址
             * */
            @Override
            public void onPageFinished(final WebView view, String url) {

                new Handler().postDelayed(() -> {
                    marketWebView.setVisibility(View.VISIBLE);
                    lodingDialog.dismiss();
                }, 100);
                wvSettings.setBlockNetworkImage(false);
                super.onPageFinished(view, url);
            }

            /**
             * 自己定义加载错误处理效果，比如：TeachCourse定义在没有网络时候，显示一张无网络的图片
             * API 23 之后调用
             */
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.setVisibility(View.GONE);
                super.onReceivedError(view, request, error);

            }

            /**
             * 自己定义加载错误处理效果，比如：TeachCourse定义在没有网络时候，显示一张无网络的图片
             *API 23之前调用
             */
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.setVisibility(View.GONE);
                super.onReceivedError(view, errorCode, description, failingUrl);
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
