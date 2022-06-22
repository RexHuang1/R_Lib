package com.dev.rexhuang.rlib.log;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.rexhuang.rlib.util.RDisplayUtil;

/**
 * *  created by RexHuang
 * *  on 2020/6/1
 */
public class RLogViewPrinterProvider {

    private FrameLayout mRootView;
    private View floatingView;
    private RecyclerView mRecyclerView;
    private boolean isOpen;
    private FrameLayout logView;

    public RLogViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView) {
        mRootView = rootView;
        mRecyclerView = recyclerView;
    }

    private static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";
    private static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";

    public void showFloatingView() {
        if (mRootView.findViewWithTag(TAG_FLOATING_VIEW) != null) {
            return;
        }

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = genFloatingView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        params.bottomMargin = RDisplayUtil.dp2px(100, mRootView.getResources());
        mRootView.addView(floatingView, params);
    }


    private View genFloatingView() {
        if (floatingView != null) {
            return floatingView;
        }
        TextView textView = new TextView(mRootView.getContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    showLogView();
                }
            }
        });
        textView.setText("RLog");
        return floatingView = textView;
    }

    private void showLogView() {
        if (mRootView.findViewWithTag(TAG_LOG_VIEW) != null) {
            return;
        }

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RDisplayUtil.dp2px(160, mRootView.getResources()));
        params.gravity = Gravity.BOTTOM;
        View logView = genLogView();
        logView.setTag(TAG_LOG_VIEW);
        mRootView.addView(genLogView(), params);
        isOpen = true;
    }

    private View genLogView() {
        if (logView != null) {
            return logView;
        }
        FrameLayout logView = new FrameLayout(mRootView.getContext());
        logView.setBackgroundColor(Color.BLACK);
        logView.addView(mRecyclerView);

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        TextView closeView = new TextView(mRootView.getContext());
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLogView();
            }
        });
        closeView.setText("Close");
        logView.addView(closeView, params);
        return this.logView = logView;
    }

    /**
     * 展示Log 悬浮按钮
     */
    public void closeFloatingView() {
        mRootView.removeView(genFloatingView());
    }

    private void closeLogView() {
        isOpen = false;
        mRootView.removeView(genLogView());
    }
}
