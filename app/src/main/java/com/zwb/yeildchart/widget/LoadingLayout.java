package com.zwb.yeildchart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwb.yeildchart.R;


public class LoadingLayout extends FrameLayout {

    /**
     * 空数据View
     */
    private int mEmptyView;
    /**
     * 状态View
     */
    private int mFailedView;
    /**
     * 加载View
     */
    private int mLoadingView;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingLayout, 0, 0);
        try {
            mLoadingView = a.getResourceId(R.styleable.LoadingLayout_loadingView, R.layout.layout_base_loading);
            mFailedView = a.getResourceId(R.styleable.LoadingLayout_stateView, R.layout.layout_base_reload);
            mEmptyView = a.getResourceId(R.styleable.LoadingLayout_emptyView, R.layout.layout_base_empty);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(mLoadingView, this, true);
            inflater.inflate(mFailedView, this, true);
            inflater.inflate(mEmptyView, this, true);
            setId(R.id.root_view);
        } finally {
            a.recycle();
        }
    }

    /**
     * 布局加载完成后隐藏所有View
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount() - 1; i++) {
            getChildAt(i).setVisibility(GONE);
        }
    }


    /**
     * 设置Empty点击事件
     * @param listener
     */
    public void setEmptyClickListener(final OnClickListener listener) {
        if( listener!=null )
            findViewById(R.id.empty_retry).setOnClickListener(listener);
    }

    /**
     * 设置State点击事件
     * @param listener
     */
    public void setReloadClickListener( OnClickListener listener ){
        if(listener!=null)
            findViewById(R.id.failed_retry).setOnClickListener(listener);
    }

    /**
     * Empty view
     */
    public void showEmpty() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 2) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    RotateAnimation loadAnimation;
    ImageView ivLoading;
    /**
     * Loading view
     */
    public void showLoading(String msg) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 0) {
                //加载动画
                ivLoading = (ImageView) child.findViewById(R.id.iv_loading);
                loadAnimation = new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                loadAnimation.setDuration(2000);
                loadAnimation.setInterpolator(new LinearInterpolator());
                loadAnimation.setRepeatCount(Animation.INFINITE);//设置重复次数：无数次
                loadAnimation.setRepeatMode(Animation.RESTART);
                ivLoading.startAnimation(loadAnimation);
                TextView mMsgView = (TextView) child.findViewById(R.id.tv_message);
                mMsgView.setText(msg);
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    /**
     * Empty view
     *
     * @param text
     */
    public void showEmpty(String text) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 2) {
                child.setVisibility(VISIBLE);
                ((TextView) child.findViewById(R.id.empty_retry)).setText(text + "");
            } else {
                child.setVisibility(GONE);
            }
        }
    }


    /**
     *
     * @param tips
     */
    public void showFailed(String tips) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == 1) {
                child.setVisibility(VISIBLE);
                ((TextView) child.findViewById(R.id.failed_retry)).setText(tips + "");
            } else {
                child.setVisibility(GONE);
            }
        }
    }

    /**
     * 展示内容
     */
    public void showContent() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i > 2 ) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }
}