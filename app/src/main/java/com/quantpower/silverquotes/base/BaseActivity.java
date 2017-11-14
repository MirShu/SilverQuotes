package com.quantpower.silverquotes.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ShuLin on 2017/6/1.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Activity mContext;
    private BaseActivity oContext;
    public BaseApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (application == null) {
            // 得到Application对象
            application = (BaseApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        this.initView();
        this.initData();
        this.fillView();
        this.bindListener();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 点击退出当前activity
     */
    public View.OnClickListener mGoBack = v -> finish();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 加载数据
     */
    public abstract void initData();

    /**
     * 填充视图数据
     */
    public abstract void fillView();

    /**
     * 绑定事件
     */
    public void bindListener() {

    }

}
