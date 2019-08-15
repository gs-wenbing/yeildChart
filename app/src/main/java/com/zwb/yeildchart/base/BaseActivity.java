package com.zwb.yeildchart.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zwb.yeildchart.R;


/**
 * base
 *
 * @author
 * @date
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 刷新
     */
    public static final int ACTION_REFRESH = 1;
    /**
     * 加载更多
     */
    public static final int ACTION_LOAD_MORE = 2;
    /**
     * 首次加载
     */
    public static final int ACTION_DEFAULT = 3;
    /**
     * 加载失败
     */
    public static final int ACTION_LOAD_FAILED = 4;

    /**
     * 返回按钮  标题按钮  右边按钮
     */
    protected TextView tvBack, tvTitle, tvRight;
    protected ImageView ivRight;
    protected LinearLayout llRight;
    public Context mContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);

        setContentView(initPageLayoutID());
        // 将每一个新开的acitivity放在activity管理集合中
        mContext = this;

        initSystemBar();
        initBarHeight();
        setBarBackground(R.color.colorPrimary);//

        getIntentData();

        initTile();
        initPageView();
        initListener();
        initData();
    }

    protected void initData() {

    }

    private void initTile() {
        View view = $(R.id.layout_title);
        if (view == null) {
            return;
        }

        view.setVisibility(View.VISIBLE);
        tvBack = $(R.id.tv_back);
        if (tvBack != null) {
            // 返回按钮监听
            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        tvTitle = $(R.id.tv_title);
        tvRight = $(R.id.tv_right);
        ivRight = $(R.id.iv_right);
        llRight = $(R.id.ll_right);
    }
    /**
     * 状态栏颜色设置
     */
    protected void initSystemBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                //隐藏标题栏
                WindowManager.LayoutParams winParams = window.getAttributes();
                final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                winParams.flags |= bits;
                window.setAttributes(winParams);
            }
        }
    }

    protected View view;

    /**
     * 状态栏设置高度
     */
    protected final void initBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                view = this.findViewById(R.id.view_StatusBar);
                if (view != null) {
                    view.getLayoutParams().height = getStatusBarHeight(this);
                    view.requestFocus();
                }
            }
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏颜色
     *
     * @param resId
     */
    protected void setBarBackground(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置 paddingTop
                ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setPadding(0, getStatusBarHeight(mContext), 0, 0);
                //直接设置状态栏颜色
                getWindow().setStatusBarColor(getResources().getColor(resId));
            } else {
                //占位状态栏
                if (view != null) {
                    view.setBackgroundResource(resId);
                }
            }
        }
    }


    /***
     * 设置title标题
     */
    public void setTitleText(String title) {
        if(tvTitle!=null){
            tvTitle.setText(title);
        }
    }

    protected void setBackVisible(boolean visible){
        if(tvBack!=null){
            tvBack.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
        }
    }

    protected void setRightVisible(boolean visible){
        if(llRight!=null){
            llRight.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int initPageLayoutID();

    /**
     * 获取Intent数据
     */
    protected abstract void getIntentData();

    /**
     * 初始化布局
     */
    protected abstract void initPageView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
