package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.adapter.RecyclerAdapter;
import com.quantpower.silverquotes.adapter.RecyclerViewHolder;
import com.quantpower.silverquotes.constant.URLS;
import com.quantpower.silverquotes.model.MessageResult;
import com.quantpower.silverquotes.model.NewsModel;
import com.quantpower.silverquotes.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShuLin on 2017/5/17.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class CollectionActivity extends Activity {
    private RecyclerAdapter<NewsModel> myOrderReycAdapter;
    private List<NewsModel> myOrderReycList = new ArrayList<>();
    RecyclerView recyclerView;
    XRefreshView xrefreshview;
    ImageView imageBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collectionactivity);
        this.xrefreshview = (XRefreshView) findViewById(R.id.xrefreshview);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imageBack = (ImageView) findViewById(R.id.image_back);
        imageBack.setOnClickListener(v -> finish());
        this.getData();
        this.bindRecycleView();
        this.xRefreshView();
    }


    private void bindRecycleView() {
        this.myOrderReycAdapter = new RecyclerAdapter<NewsModel>(CollectionActivity.this, myOrderReycList,
                R.layout.item_news) {
            @Override
            public void convert(RecyclerViewHolder helper, NewsModel item, int position) {
                ImageView news_img = helper.getView(R.id.news_img);
                helper.setText(R.id.news_title, item.getNews_title());
                helper.setText(R.id.news_time, item.getNews_time_show());
                helper.setText(R.id.news_readcount, item.getNews_readcount());
                helper.setText(R.id.news_type, item.getNews_type());
                helper.setImageUrl(R.id.news_img, item.getNews_img());
                news_img.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        };

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(CollectionActivity.this, 1,
                LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(this.myOrderReycAdapter);
        this.myOrderReycAdapter.setOnItemClickListener((parent, position) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(NewsDetailActivity.NEWS_ID_TAG, myOrderReycList.get(position).getId());
            UIHelper.startActivity(CollectionActivity.this, NewsDetailActivity.class, bundle);

        });

    }

    private void getData() {
        RequestParams params = new RequestParams(URLS.NEWS_NEWS_LIST);
        params.addBodyParameter("news_time", String.valueOf(0));
        params.addBodyParameter("type", "2,4,7,8,9,12");
        params.addBodyParameter("read", String.valueOf(0));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    MessageResult message = MessageResult.parse(result);
                    List<NewsModel> newsList = JSON.parseArray(message.getData(),
                            NewsModel.class);
                    myOrderReycList.addAll(newsList);
                    myOrderReycAdapter.notifyDataSetChanged();
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

    /**
     * 刷新机制
     */
    private void xRefreshView() {
        this.xrefreshview.setAutoRefresh(true);
        this.xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> xrefreshview.stopRefresh(), 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {

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
