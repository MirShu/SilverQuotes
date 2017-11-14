package com.quantpower.silverquotes.ui.fragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.adapter.RecyclerAdapter;
import com.quantpower.silverquotes.adapter.RecyclerViewHolder;
import com.quantpower.silverquotes.ui.activity.ProductIntroductionActivity;
import com.quantpower.silverquotes.utils.Constants;
import com.quantpower.silverquotes.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShuLin on 2017/5/19.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class FindFragment extends Fragment {
    public static FindFragment newInstance(String s) {
        FindFragment homeFragment = new FindFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    RecyclerView recyclerView;
    private RecyclerAdapter myOrderReycAdapter;
    private List<String> myOrderReycList;
    XRefreshView xrefreshview;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_find, container, false);
        this.xrefreshview = (XRefreshView) rootView.findViewById(R.id.xrefreshview);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.bindRecycleView();
        this.xRefreshView();
        return rootView;
    }


    private void bindRecycleView() {
        this.getData();
        this.myOrderReycAdapter = new RecyclerAdapter<String>(getActivity(), myOrderReycList,
                R.layout.item_found) {
            @Override
            public void convert(RecyclerViewHolder helper, String item, int position) {
                TextView tvState = helper.getView(R.id.tv_name);
                RelativeLayout rlItemBg = helper.getView(R.id.rl_item_bg);
                TextView tvProductLabel = helper.getView(R.id.tv_product_label);
                if (position == 0) {
                    tvState.setText("恒生指数");
                    tvProductLabel.setText("已关注");
                    Resources resources = getContext().getResources();
                    Drawable btnDrawable = ContextCompat.getDrawable(context, R.mipmap.bg_hang_seng_index);
                    rlItemBg.setBackgroundDrawable(btnDrawable);
                } else if (position == 1) {
                    tvState.setText("原油期货");
                    tvProductLabel.setText("+关注");
                    Resources resources = getContext().getResources();
                    Drawable btnDrawable = ContextCompat.getDrawable(context, R.mipmap.bg_crude_index);
                    rlItemBg.setBackgroundDrawable(btnDrawable);
                } else if (position == 2) {
                    tvState.setText("黄金期货");
                    tvProductLabel.setText("+关注");
                    Resources resources = getContext().getResources();
                    Drawable btnDrawable = ContextCompat.getDrawable(context, R.mipmap.bg_gold_index);
                    rlItemBg.setBackgroundDrawable(btnDrawable);
                } else if (position == 3) {
                    tvState.setText("白银期货");
                    tvProductLabel.setText("已关注");
                    Drawable btnDrawable = ContextCompat.getDrawable(context, R.mipmap.bg_silver_index);
                    rlItemBg.setBackgroundDrawable(btnDrawable);
                }
            }
        };

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1,
                LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(this.myOrderReycAdapter);
        this.myOrderReycAdapter.setOnItemClickListener((parent, position) -> {
            Bundle bundle = new Bundle();
            int tag = Integer.valueOf(myOrderReycList.get(position));
            bundle.putInt(ProductIntroductionActivity.ORDER_TAG, tag);
            UIHelper.startActivity(getActivity(), ProductIntroductionActivity.class, bundle);
        });

    }

    private void getData() {
        myOrderReycList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            myOrderReycList.add("" + i);
        }

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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());       //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}
