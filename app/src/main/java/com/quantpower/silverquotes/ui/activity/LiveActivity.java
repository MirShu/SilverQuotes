package com.quantpower.silverquotes.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.base.BaseActivity;
import com.quantpower.silverquotes.constant.URLS;
import com.quantpower.silverquotes.model.LiveRoomModel;
import com.quantpower.silverquotes.widget.MNViderPlayer;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by ShuLin on 2017/5/20.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class LiveActivity extends BaseActivity {
    private TextView tv_head_title;
//    private RelativeLayout rl_head;

    private ImageView iv_back_icon;
    private TextView tv_title;

    private WebView live_web;

    public static final String ROOM_ID = "room_id";

    public static final String TOKEN = "token";

    private int room_id;

    private String token;

    private String url;

    private boolean isAll = false;

    private MNViderPlayer mnViderPlayer;

    private RelativeLayout rl_radio;


    @Override
    public void initView() {
//        setContentView(R.layout.collectionactivity);
        setContentView(R.layout.activity_live);
        this.iv_back_icon = (ImageView) findViewById(R.id.iv_back_icon);
//        this.rl_head = (RelativeLayout) findViewById(R.id.rl_head1);
        this.live_web = (WebView) findViewById(R.id.live_web);
        this.mnViderPlayer = (MNViderPlayer) findViewById(R.id.mn_videoplayer);
        this.live_web.getSettings().setJavaScriptEnabled(true);
        this.room_id = getIntent().getIntExtra(ROOM_ID, 0);
        this.token = getIntent().getStringExtra("token");
        rl_radio = (RelativeLayout) findViewById(R.id.rl_radio);
        iv_back_icon.bringToFront();

        //获取屏幕的宽高
        WindowManager windowManager = this.getWindowManager();
        int height = windowManager.getDefaultDisplay().getHeight();
        int weith = windowManager.getDefaultDisplay().getWidth();

        this.iv_back_icon.setOnClickListener(view -> {
            mnViderPlayer.destroyVideo();
            mnViderPlayer = null;
            finish();
        });
        getUri();

        //动态设置webview的高度.
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) live_web.getLayoutParams();
//        layout.height = height - DensityUtil.dip2px(this, 200);
        layout.height = height- DensityUtil.dip2px(200);
        live_web.setLayoutParams(layout);
    }

    //获取请求数据
    private void getUri() {
        RequestParams params = new RequestParams(URLS.LIVE_GET_LIVEVIDEO);
//        params.addBodyParameter("room_id", String.valueOf(room_id));
        params.addBodyParameter("room_id", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                LiveRoomModel response = gson.fromJson(result, LiveRoomModel.class);
                if (response.getCode().equals("0")) {
                    url = response.getData().getVideosource();
                    initPlayer();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });


    }

    @Override
    public void initData() {
//        live_web.loadUrl(URLS.LIVE_INDEX + "?token=" + token + "&room_id=" + room_id);
        live_web.loadUrl("http://api.quant-power.com/Live/index");
        live_web.getSettings().setPluginState(WebSettings.PluginState.ON);
        live_web.setWebChromeClient(new WebChromeClient());
        live_web.getSettings().setLoadWithOverviewMode(true);
        live_web.getSettings().setUseWideViewPort(true);
    }

    private void initPlayer() {
        mnViderPlayer.setIsNeedBatteryListen(true);//电量监听
        mnViderPlayer.setIsNeedNetChangeListen(true);//网络监听
        mnViderPlayer.setDataSource(url, "");
        mnViderPlayer.setOnNetChangeListener(new MNViderPlayer.OnNetChangeListener() {
            @Override
            public void onWifi(MediaPlayer mediaPlayer) {
            }

            @Override
            public void onMobile(MediaPlayer mediaPlayer) {
                Toast.makeText(LiveActivity.this, "请注意,当前网络状态切换为3G/4G网络", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNoAvailable(MediaPlayer mediaPlayer) {
                Toast.makeText(LiveActivity.this, "当前网络不可用,检查网络设置", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void btn01(View view) {
        mnViderPlayer.playVideo(url, "");
    }

    @Override
    public void fillView() {
        live_web.setBackgroundColor(0);
        live_web.setOnLongClickListener(view -> true);
        live_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mnViderPlayer.isFullScreen()) {
            mnViderPlayer.setOrientationPortrait();
            return;
        } else {
            mnViderPlayer.destroyVideo();
            mnViderPlayer = null;
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);//友盟统计
        if (mnViderPlayer != null) {
            mnViderPlayer.pauseVideo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPause(this);//友盟统计
    }

    @Override
    protected void onDestroy() {
        //一定要记得销毁View
        if (mnViderPlayer != null) {
            mnViderPlayer.destroyVideo();
            mnViderPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            live_web.setVisibility(View.VISIBLE);
            iv_back_icon.setVisibility(View.VISIBLE);
        } else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LiveActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            live_web.setVisibility(View.GONE);
            iv_back_icon.setVisibility(View.GONE);
        }
    }
}
