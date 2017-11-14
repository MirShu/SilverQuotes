package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.adapter.RecyclerAdapter;
import com.quantpower.silverquotes.adapter.RecyclerViewHolder;
import com.quantpower.silverquotes.constant.URLS;
import com.quantpower.silverquotes.model.MessageResult;
import com.quantpower.silverquotes.model.NewsModel;
import com.quantpower.silverquotes.utils.PrefUtils;
import com.quantpower.silverquotes.utils.UIHelper;
import com.quantpower.silverquotes.widget.LodingDialog;
import com.quantpower.silverquotes.widget.TBAlertDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.util.EncodingUtils;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    public static final String NEWS_ID_TAG = "news_id_tag";
    public static final String NEWS_TYPE = "news_type";
    public static final String NEWS_TIPS = "news_tips";
    private int newsId;
    private int newsType;
    private int newsTips;
    private LinearLayout ll_title;
    private RecyclerView rv_related;
    private RecyclerAdapter<NewsModel> rvRelatedAdapter;
    private List<NewsModel> listNewsRelevant = new ArrayList<>();

    private TextView detail_news_title;
    private TextView detail_news_time;
    private TextView detail_news_by;
    private WebView detail_webView;
    private CheckBox collectionChec;
    private RelativeLayout rl_font;
    private RelativeLayout rl_foot;
    private LodingDialog lodingDialog;
    private NewsModel newsDatail;
    private PopupWindow popWindow;
    protected String url;
    private TextView tv_original_link;
    private TextView tvComplaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        this.initView();
        this.initData();
    }

    public void initView() {
        this.rv_related = (RecyclerView) findViewById(R.id.rvRelated);
        this.ll_title = (LinearLayout) findViewById(R.id.ll_title);
        this.rl_font = (RelativeLayout) findViewById(R.id.rl_font);
        this.detail_news_title = (TextView) findViewById(R.id.detail_news_title);
        this.detail_news_time = (TextView) findViewById(R.id.detail_news_time);
        this.detail_news_by = (TextView) findViewById(R.id.detail_news_by);
        this.detail_webView = (WebView) findViewById(R.id.detail_webView);
        this.collectionChec = (CheckBox) findViewById(R.id.iv_disable);
        this.rl_foot = (RelativeLayout) findViewById(R.id.rl_foot);
        this.tvComplaint = (TextView) findViewById(R.id.tv_complaint);
        this.tv_original_link = (TextView) findViewById(R.id.tv_original_link);
        this.ll_title.setFocusable(true);
        this.ll_title.setFocusableInTouchMode(true);
        this.ll_title.requestFocus();
        this.newsId = getIntent().getIntExtra(NEWS_ID_TAG, 0);
        this.newsType = getIntent().getIntExtra(NEWS_TYPE, 0);
        this.newsTips = getIntent().getIntExtra(NEWS_TIPS, 0);
        if (newsTips == 1) {
            tv_original_link.setVisibility(View.GONE);
        } else {
            tv_original_link.setVisibility(View.VISIBLE);
        }
        WebSettings wvSettings = detail_webView.getSettings();
        wvSettings.setJavaScriptEnabled(true);
        this.bindRecycleView();
        this.webView();
        this.lodingDialog = new LodingDialog(NewsDetailActivity.this, "加载中...");
        this.lodingDialog.show();
        new Handler().postDelayed(() -> lodingDialog.dismiss(), 1000);
        tvComplaint.setOnClickListener(v -> complaintDialog());
    }

    //清除缓存
    private void complaintDialog() {
        new TBAlertDialog(NewsDetailActivity.this).builder().setTitle("提示")
                .setMsg("确定投诉此文章吗？")
                .setPositiveButton("确认", v -> UIHelper.toastMessage(NewsDetailActivity.this, "投诉成功")).setNegativeButton("取消", v -> {
        }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*********************
     * 绑定相关资讯列表数据
     ********************/
    private void bindRecycleView() {
        this.rvRelatedAdapter = new RecyclerAdapter<NewsModel>(this, listNewsRelevant, R.layout.item_news) {
            @Override
            public void convert(RecyclerViewHolder helper, NewsModel item, int position) {
                ImageView news_img = helper.getView(R.id.news_img);
                news_img.setScaleType(ImageView.ScaleType.FIT_XY);
                helper.setImageUrl(R.id.news_img, item.getNews_img());
                helper.setText(R.id.news_title, item.getNews_title());
            }
        };
        this.rv_related.setHasFixedSize(true);
        this.rv_related.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        this.rv_related.setAdapter(this.rvRelatedAdapter);

        this.rvRelatedAdapter.setOnItemClickListener((parent, position) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(NewsDetailActivity.NEWS_ID_TAG, listNewsRelevant.get(position).getId());
            UIHelper.startActivity(this, NewsDetailActivity.class, bundle);
        });
    }

    /****************************
     * 详情界面头部加载H5代码块
     ***************************/
    private void webView() {
        RequestParams params = new RequestParams(URLS.NEWS_NEWSINFO);
        params.addBodyParameter("id", "" + newsId);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MessageResult message = MessageResult.parse(result);
                newsDatail = NewsModel.parse(message.getData());
                detail_news_title.setText(newsDatail.getNews_title());
                detail_news_time.setText(newsDatail.getNews_time());
                detail_news_by.setText(newsDatail.getNews_by());
                String content = newsDatail.getNews_content();
                url = newsDatail.getNews_detail_url();
                content = content.replace("style", "");
                content = content.replace("width", "").replace("<img", "<img style='max-width:100% '");
                content = "<style>a{color:black;text-decoration:none;}</style>" + content;
                String newsContent = "<html><head><style type=\"text/css\">body " +
                        "{text-align:justify; font-size:px; line-height: " + (30) + "px;color:#666666}</style></head> \n" +
                        "<body>" + EncodingUtils.getString(content.getBytes(), "UTF-8") + "</body> \n </html>";
                detail_webView.loadDataWithBaseURL(null, newsContent,
                        "text/html", "utf-8", null);
                SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
                String newsSize = sp.getString("news_size", "");
                WebSettings fontLargestStyle = detail_webView.getSettings();
                if (newsSize.equals("SMALLER")) {
                    fontLargestStyle.setTextZoom(70);
                }
                if (newsSize.equals("LARGER")) {
                    fontLargestStyle.setTextZoom(130);
                }
                if (newsSize.equals("NORMAL")) {
                    fontLargestStyle.setTextZoom(100);
                }
                if (newsSize.isEmpty()) {
                    fontLargestStyle.setTextZoom(100);
                }
                String state = newsDatail.getCollstate();
                if (state.equals("1")) {
                    collectionChec.isChecked();
                    collectionChec.setChecked(true);
                } else if (state.equals("0")) {
                    collectionChec.setChecked(false);
                }
                detail_webView.getSettings().setAppCacheEnabled(true);// 设置启动缓存
                if (url == null) {
                    tv_original_link.setVisibility(View.GONE);
                } else {
                    tv_original_link.setVisibility(View.VISIBLE);
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
        detail_webView.setOnLongClickListener(view -> true);
    }

    /******************************
     * 获取详情界面底部相关资讯列表
     *****************************/
    public void initData() {
        String nsType = String.valueOf(newsType + 1);
        RequestParams params = new RequestParams(URLS.NEWS_NEWSRELEVANT);
        params.addBodyParameter("id", String.valueOf(newsId));
        if (nsType.equals("13")) {
            params.addBodyParameter("type", "12");
        } else {
            params.addBodyParameter("type", nsType);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    MessageResult message = MessageResult.parse(result);
                    List<NewsModel> newsList = JSON.parseArray(message.getData(),
                            NewsModel.class);
                    listNewsRelevant.addAll(newsList);
                    rvRelatedAdapter.notifyDataSetChanged();
                } catch (Exception e) {

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

    /*************************
     * 下标栏按钮点击处理事件
     ************************/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_font:
                fontStyle();
                break;
            case R.id.tv_original_link:
                Bundle bundle = new Bundle();
                bundle.putString(OriginalPageActivity.ORIGINALLINK, url);
                UIHelper.startActivity(NewsDetailActivity.this, OriginalPageActivity.class, bundle);
                break;
        }
    }

    /***************
     * 修改字体大小
     ***************/
    private void fontStyle() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.popu_fontstyle_menu, null);
        TextView font_min = (TextView) popView.findViewById(R.id.font_min);
        TextView font_default = (TextView) popView.findViewById(R.id.font_default);
        TextView font_max = (TextView) popView.findViewById(R.id.font_max);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        popWindow = new PopupWindow(popView, width, 500);
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        String newsSize = sp.getString("news_size", "");

        if (newsSize.equals("SMALLER")) {
            font_min.setBackgroundResource(R.drawable.shape_font_min_press);
            font_default.setBackgroundResource(R.drawable.shape_font);
            font_max.setBackgroundResource(R.drawable.shape_font);
            font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
            font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
            font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
        }
        if (newsSize.equals("LARGER")) {
            font_max.setBackgroundResource(R.drawable.shape_font_max_press);
            font_min.setBackgroundResource(R.drawable.shape_font);
            font_default.setBackgroundResource(R.drawable.shape_font);
            font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
            font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
            font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
        }
        if (newsSize.equals("NORMAL")) {
            font_default.setBackgroundResource(R.drawable.shape_font_press);
            font_min.setBackgroundResource(R.drawable.shape_font);
            font_max.setBackgroundResource(R.drawable.shape_font);
            font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
            font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
            font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
        }
        if (newsSize.isEmpty()) {
            font_default.setBackgroundResource(R.drawable.shape_font_press);
            font_min.setBackgroundResource(R.drawable.shape_font);
            font_max.setBackgroundResource(R.drawable.shape_font);
            font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
            font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
            font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
        }
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);

        View.OnClickListener listener = v -> {
            switch (v.getId()) {
                case R.id.font_min:
                    font_min.setBackgroundResource(R.drawable.shape_font_min_press);
                    font_default.setBackgroundResource(R.drawable.shape_font);
                    font_max.setBackgroundResource(R.drawable.shape_font);
                    font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
                    font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
                    font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
                    WebSettings fontSmallestStyle = detail_webView.getSettings();
                    fontSmallestStyle.setTextZoom(70);
                    PrefUtils.setString(NewsDetailActivity.this, "news_size", "SMALLER");
                    break;
                case R.id.font_default:
                    font_default.setBackgroundResource(R.drawable.shape_font_press);
                    font_min.setBackgroundResource(R.drawable.shape_font);
                    font_max.setBackgroundResource(R.drawable.shape_font);
                    font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
                    font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
                    font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
                    WebSettings fontNormalStyle = detail_webView.getSettings();
                    fontNormalStyle.setTextZoom(100);
                    PrefUtils.setString(NewsDetailActivity.this, "news_size", "NORMAL");
                    break;
                case R.id.font_max:
                    font_max.setBackgroundResource(R.drawable.shape_font_max_press);
                    font_min.setBackgroundResource(R.drawable.shape_font);
                    font_default.setBackgroundResource(R.drawable.shape_font);
                    font_max.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_fefefe));
                    font_min.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
                    font_default.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_47a4f2));
                    WebSettings fontLargestStyle = detail_webView.getSettings();
                    fontLargestStyle.setTextZoom(130);
                    PrefUtils.setString(NewsDetailActivity.this, "news_size", "LARGER");
                    break;
            }
        };

        font_min.setOnClickListener(listener);
        font_default.setOnClickListener(listener);
        font_max.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30fafafa);
        popWindow.setBackgroundDrawable(dw);
        int[] location = new int[2];
        rl_foot.getLocationOnScreen(location);
        popWindow.showAtLocation(rl_foot, Gravity.NO_GRAVITY, location[0], location[1] - popWindow.getHeight());
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
