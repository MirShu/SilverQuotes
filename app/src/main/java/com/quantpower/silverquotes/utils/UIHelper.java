package com.quantpower.silverquotes.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.quantpower.silverquotes.R;


/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public class UIHelper {
    private static Handler mHandler = new Handler();
    /**
     * 弹出Toast消息
     */
    public static void toastMessage(Context context, String msg) {
        toastMessage(context, msg, 1500);
    }
    /**
     * 正常启动Activity
     */
    public static void startActivity(Activity activity, Class<? extends Activity> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
    }

    /**
     * @param context
     * @param intent
     */
    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 携带数据启动Activity
     */
    public static void startActivity(Activity activity, Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * 启动Activity，获取数据
     */
    public static void startActivityForResult(Activity activity, Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        try {
            Intent intent = new Intent(activity, clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception ex) {

        }
    }

    /**
     * @param activity
     * @param intent
     * @param requestCode
     * @param bundle
     */
    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle bundle) {
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 弹出拨打电话Activity
     *
     * @param context
     * @param phone
     */
    public static void showTel(Activity context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * @param mContext
     * @param msg
     * @param duration
     */
    private static void toastMessage(Context mContext, CharSequence msg, int duration) {
        msg = msg.toString().replace("。", "").replace("，", ",");
        Toast toast = new Toast(mContext);
//        toast.setGravity(Gravity.BOTTOM , 0, DensityUtil.dip2px(mContext,100));
        toast.setDuration(duration);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_toast, null);
        TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
        tv_toast.setText(msg);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0, 0, 0, 200);
//        tv_toast.setLayoutParams(layoutParams);
        toast.setView(view);
        toast.show();
    }
}
