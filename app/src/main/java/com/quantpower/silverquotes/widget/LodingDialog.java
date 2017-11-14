package com.quantpower.silverquotes.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantpower.silverquotes.R;


/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */
public class LodingDialog extends Dialog {
    private String content;

    public LodingDialog(Context context, String content) {
        super(context, R.style.CustomDialog);
        this.content = content;
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (LodingDialog.this.isShowing())
                    LodingDialog.this.dismiss();
                break;
        }
        return true;
    }

    private void initView() {
        setContentView(R.layout.view_dialog);
        ((TextView) findViewById(R.id.tvcontent)).setText(content);
        ImageView iv_dialog = (ImageView) findViewById(R.id.iv_dialog);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_dialog.getDrawable();
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.9f;
        animationDrawable.start();
        getWindow().setAttributes(attributes);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        setCancelable(false);
    }
}
