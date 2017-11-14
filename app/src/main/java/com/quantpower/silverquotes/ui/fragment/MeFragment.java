package com.quantpower.silverquotes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.ui.activity.AboutActivity;
import com.quantpower.silverquotes.ui.activity.CollectionActivity;
import com.quantpower.silverquotes.ui.activity.OpinionActivity;
import com.quantpower.silverquotes.utils.Constants;
import com.quantpower.silverquotes.utils.UIHelper;
import com.quantpower.silverquotes.widget.TBAlertDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ShuLin on 2017/5/19.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class MeFragment extends Fragment {
    public static MeFragment newInstance(String s) {
        MeFragment homeFragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    private TextView tvMeCollection;
    private TextView tvMeOpinion;
    private TextView tvMeAbout;
    private TextView tvMeClean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        tvMeCollection = (TextView) view.findViewById(R.id.tv_me_collection);
        tvMeOpinion = (TextView) view.findViewById(R.id.tv_me_opinion);
        tvMeAbout = (TextView) view.findViewById(R.id.tv_me_about);
        tvMeClean = (TextView) view.findViewById(R.id.tv_me_clean);
        tvMeCollection.setOnClickListener(v -> UIHelper.startActivity(getActivity(), CollectionActivity.class));
        tvMeOpinion.setOnClickListener(v -> UIHelper.startActivity(getActivity(), OpinionActivity.class));
        tvMeAbout.setOnClickListener(v -> UIHelper.startActivity(getActivity(), AboutActivity.class));
        tvMeClean.setOnClickListener(v -> clearCacheDialog());
        return view;
    }

    //清除缓存
    private void clearCacheDialog() {
        new TBAlertDialog(getActivity()).builder().setTitle("提示")
                .setMsg("确定清除缓存？")
                .setPositiveButton("确认", v -> UIHelper.toastMessage(getActivity(), "清除成功")).setNegativeButton("取消", v -> {
        }).show();
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
