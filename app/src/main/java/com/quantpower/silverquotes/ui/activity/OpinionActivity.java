package com.quantpower.silverquotes.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantpower.silverquotes.R;
import com.quantpower.silverquotes.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by ShuLin on 2017/5/17.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class OpinionActivity extends Activity {
    ImageView imageBack;
    TextView suBmit;
    EditText edDetailed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opinionactivity);
        imageBack = (ImageView) findViewById(R.id.image_back);
        suBmit = (TextView) findViewById(R.id.submit);
        imageBack.setOnClickListener(v -> finish());
        edDetailed = (EditText) findViewById(R.id.ed_detailed);
        String content = edDetailed.getText().toString().trim();
        suBmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(content)) {
                UIHelper.toastMessage(OpinionActivity.this, "意见提交成功");
                return;
            } else if (!TextUtils.isEmpty(content)) {
                UIHelper.toastMessage(OpinionActivity.this, "意见提交成功");
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
