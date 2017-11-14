package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.model.MessageResult;
import com.quantpower.silverquotes.utils.PhoneLegal;
import com.quantpower.silverquotes.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class GuideActivity extends Activity {
    private ImageView point;
    private ViewPager mViewPager;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private ArrayList<ImageView> mImageViewList;
    private int[] mImageIds = new int[]{R.mipmap.guide_one, R.mipmap.guide_two, R.mipmap.guide_three, R.mipmap.guide_four
    };


    private EditText etPhone;
    private EditText etCode;
    private TextView btIdent;
    private TextView btLogin;
    private TimeCount time;
    private int mPointDis;
    private LinearLayout llPopuLogin;
    private String getCodeUrl = "http://api.quant-power.com/User/SendCode";
    private String loGinUrl = "http://api.quant-power.com/User/userRegisterLogin";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        llPopuLogin = (LinearLayout) findViewById(R.id.ll_popu_login);

        etCode = (EditText) findViewById(R.id.et_code);
        etPhone = (EditText) findViewById(R.id.et_register_phone);
        btIdent = (TextView) findViewById(R.id.tv_ident);
        btLogin = (TextView) findViewById(R.id.bt_login);
        time = new GuideActivity.TimeCount(60000, 1000);
        btIdent.setOnClickListener(v -> indentClick());
        btLogin.setOnClickListener(v -> loginClick());
        btLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));  //测试进入主页面
        initData();
    }

    public void initData() {
        initRecord();
        mViewPager.setAdapter(new GuideAdapter());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int leftMargin = (int) (mPointDis * positionOffset) + position
                        * mPointDis;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageViewList.size() - 1) {
                    btLogin.setVisibility(View.VISIBLE);
                    llPopuLogin.setVisibility(View.VISIBLE);
                    ivRedPoint.setVisibility(View.GONE);
                    llContainer.setVisibility(View.GONE);
                } else {
                    btLogin.setVisibility(View.GONE);
                    llPopuLogin.setVisibility(View.GONE);
                    ivRedPoint.setVisibility(View.VISIBLE);
                    llContainer.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        ivRedPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointDis = llContainer.getChildAt(1).getLeft()
                                - llContainer.getChildAt(0).getLeft();
                    }
                });

//        btLogin.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        });

    }


    public void initRecord() {
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view);
            point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                params.leftMargin = 25;
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }
    }

    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    //获取验证码点击事件
    public void indentClick() {
        etCode.setNextFocusDownId(R.id.et_code);
        String mPhone = etPhone.getText().toString().trim();
        boolean result = PhoneLegal.isPhone(mPhone);
        if (TextUtils.isEmpty(mPhone)) {
            UIHelper.toastMessage(GuideActivity.this, "请输入手机号");
            return;
        }
        if (result == false) {
            UIHelper.toastMessage(GuideActivity.this, "请输入正确的手机号");
            return;
        } else {
            etCode.requestFocus();
            etCode.getImeOptions();
            RequestParams params = new RequestParams(getCodeUrl);
            params.addBodyParameter("phone", mPhone);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    time.start();
                    MessageResult message = MessageResult.parse(result);
                    String code = String.valueOf(message.getCode());
                    if (code.equals("0")) {
                        UIHelper.toastMessage(GuideActivity.this, "获取验证码发送成功");
                    } else {
                        UIHelper.toastMessage(GuideActivity.this, "获取验证码失败");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    UIHelper.toastMessage(GuideActivity.this, "获取验证码失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    /**
     * 点击一键按钮登录事件处理
     */
    public void loginClick() {
        String mPhone = etPhone.getText().toString().trim();
        String mCode = etCode.getText().toString().trim();
        if (mPhone.isEmpty() || mCode.isEmpty()) {
            UIHelper.toastMessage(GuideActivity.this, "请将信息填写完整");
        } else {
            RequestParams params = new RequestParams(loGinUrl);
            params.addBodyParameter("phone", mPhone);
            params.addBodyParameter("code", mCode);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    MessageResult message = MessageResult.parse(result);
                    String code = String.valueOf(message.getCode());
                    if (code.equals("0")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        UIHelper.toastMessage(GuideActivity.this, "验证码错误或已过期");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    UIHelper.toastMessage(GuideActivity.this, "网络加载失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btIdent.setClickable(false);
            btIdent.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btIdent.setText("重发");
            btIdent.setClickable(true);
        }
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
