package com.quantpower.silverquotes.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.adapter.RecyclerAdapter;
import com.quantpower.silverquotes.adapter.RecyclerViewHolder;
import com.quantpower.silverquotes.constant.URLS;
import com.quantpower.silverquotes.model.MessageResult;
import com.quantpower.silverquotes.model.NewsModel;
import com.quantpower.silverquotes.ui.activity.CalendarActivity;
import com.quantpower.silverquotes.ui.activity.LiveActivity;
import com.quantpower.silverquotes.ui.activity.NewsDetailActivity;
import com.quantpower.silverquotes.ui.activity.ServiceActivity;
import com.quantpower.silverquotes.ui.activity.StrategyActivity;
import com.quantpower.silverquotes.utils.Constants;
import com.quantpower.silverquotes.utils.GlideImageLoader;
import com.quantpower.silverquotes.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.transformer.ForegroundToBackgroundTransformer;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShuLin on 2017/5/19.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance(String s) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    RecyclerView recyclerView;
    private RecyclerAdapter<NewsModel> myOrderReycAdapter;
    private List<NewsModel> myOrderReycList = new ArrayList<>();
    XRefreshView xrefreshview;
    private View rootView;
    private int readsize = 0;
    private Banner banner;
    private LinearLayout llStrategy;
    private LinearLayout llCalendar;
    private LinearLayout llLive;
    private LinearLayout llService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        banner = (Banner) rootView.findViewById(R.id.banner);
        this.xrefreshview = (XRefreshView) rootView.findViewById(R.id.xrefreshview);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.llStrategy = (LinearLayout) rootView.findViewById(R.id.ll_strategy);
        this.llCalendar = (LinearLayout) rootView.findViewById(R.id.ll_calendar);
        this.llLive = (LinearLayout) rootView.findViewById(R.id.ll_live);
        this.llService = (LinearLayout) rootView.findViewById(R.id.ll_service);


        Log.e("----------","第一个页面");
        llStrategy.setOnClickListener(v -> UIHelper.startActivity(getActivity(), StrategyActivity.class));
        llCalendar.setOnClickListener(v -> UIHelper.startActivity(getActivity(), CalendarActivity.class));
        llLive.setOnClickListener(v -> UIHelper.startActivity(getActivity(), LiveActivity.class));
        llService.setOnClickListener(v -> UIHelper.startActivity(getActivity(), ServiceActivity.class));
        this.bindRecycleView();
        this.xRefreshView();
        this.initView();
        return rootView;
    }

    private void initView() {
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.banner01);
        list.add(R.mipmap.banner02);
        list.add(R.mipmap.banner03);
        list.add(R.mipmap.banner04);
        list.add(R.mipmap.banner05);
        list.add(R.mipmap.banner06);
        banner.setImages(list)
                .setBannerAnimation(ForegroundToBackgroundTransformer.class)
                .setImageLoader(new GlideImageLoader())
                .start();
    }


    private void bindRecycleView() {
        this.getData();
        this.myOrderReycAdapter = new RecyclerAdapter<NewsModel>(getActivity(), myOrderReycList,
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
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1,
                LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(this.myOrderReycAdapter);
        this.myOrderReycAdapter.setOnItemClickListener((parent, position) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(NewsDetailActivity.NEWS_ID_TAG, myOrderReycList.get(position).getId());
            UIHelper.startActivity(getActivity(), NewsDetailActivity.class, bundle);
        });
    }

    private void getData() {
        String readSize = String.valueOf(readsize);
        RequestParams params = new RequestParams(URLS.NEWS_NEWS_LIST);
        params.addBodyParameter("news_time", String.valueOf(0));
        params.addBodyParameter("type", "2,4,7,8,9,12");
        params.addBodyParameter("read", readSize);
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
        this.xrefreshview.setPullLoadEnable(true);
        this.xrefreshview.setMoveForHorizontal(true);
        this.xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                myOrderReycList.clear();
                readsize = 0;
                getData();
                new Handler().postDelayed(() -> xrefreshview.stopRefresh(), 1000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                new Handler().postDelayed(() -> {
                    readsize += 10;
                    getData();
                }, 600);
                new Handler().postDelayed(() -> xrefreshview.stopLoadMore(), 1500);
            }

        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}
