package com.example.jadynai.globalwidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * @version:
 * @FileDescription:
 * @Author:jing
 * @Since:2017/11/17
 * @ChangeList:
 */

public class GlobalWidget {

    private static final String TAG = "GlobalToast";
    private static final int DEF_DURATION = 5;
    private Toast mToast;

    private ValueAnimator mTimeEngine;

    public View mGlobalContentView;


    private GlobalWidget() {
        mToast = new Toast(BaseApplication.getInstance());
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mGlobalContentView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.layout_global_standard, null);
        TextView toastTitle = (TextView) mGlobalContentView.findViewById(R.id.tv_global_title);
        toastTitle.setText("Title");
        ViewGroup.LayoutParams textParams = toastTitle.getLayoutParams();
        textParams.width = BaseApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
        toastTitle.setLayoutParams(textParams);
        mGlobalContentView.findViewById(R.id.img_global_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEngine();
            }
        });
        mToast.setView(mGlobalContentView);
        mTimeEngine = ValueAnimator.ofInt(0, 1).setDuration(DEF_DURATION * 1000);
        mTimeEngine.setInterpolator(new LinearInterpolator());
        mTimeEngine.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                showInternal();
            }
        });
        mTimeEngine.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hide();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                hide();
            }
        });
        try {
            Field mTN = mToast.getClass().getDeclaredField("mTN");
            mTN.setAccessible(true);
            // 反射拿到Toast的TN对象
            Object TNOb = mTN.get(mToast);
            Field paramField = TNOb.getClass().getDeclaredField("mParams");
            paramField.setAccessible(true);
            // 反射拿到TN内部的Window.LayoutParams 对象
            Object paramOb = paramField.get(TNOb);
            Field flagsField = paramOb.getClass().getDeclaredField("flags");
            // 修改Params为隐藏状态栏,且window不占用焦点。否则Toast弹出影响其他界面点击
            flagsField.set(paramOb, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GlobalWidget makeText(@NonNull String text) {
        GlobalWidget globalToast = new GlobalWidget();
        return globalToast.makeTextInternal(text, DEF_DURATION);
    }

    public static GlobalWidget makeText(@NonNull String text, @IntRange(from = 2, to = 20) int duration) {
        GlobalWidget globalToast = new GlobalWidget();
        return globalToast.makeTextInternal(text, duration);
    }

    private GlobalWidget makeTextInternal(@NonNull String text, @IntRange(from = 2, to = 20) int duration) {
        TextView toastTitle = (TextView) mToast.getView().findViewById(R.id.tv_global_title);
        if (StringUtils.differsExcludeNull(text, toastTitle.getText())) {
            toastTitle.setText(text);
        }
        if (duration != DEF_DURATION) {
            mTimeEngine.setDuration(duration * 1000);
        }
        return this;
    }

    public void show() {
        if (mTimeEngine.isRunning()) {
            return;
        }
        mTimeEngine.end();
        mTimeEngine.start();
    }

    private void showInternal() {
        TextView toastTitle = (TextView) mToast.getView().findViewById(R.id.tv_global_title);
        ViewGroup.LayoutParams textParams = toastTitle.getLayoutParams();
        textParams.width = BaseApplication.getInstance().getResources().getDisplayMetrics().widthPixels;;
        toastTitle.setLayoutParams(textParams);
        mToast.getView().setVisibility(View.VISIBLE);
        mToast.show();
    }

    public GlobalWidget setToastListener(View.OnClickListener clickListener) {
        mToast.getView().setOnClickListener(clickListener);
        return this;
    }

    private void hide() {
        mToast.getView().setVisibility(View.GONE);
        mToast.cancel();
    }

    public void closeEngine() {
        mTimeEngine.end();
    }

}

