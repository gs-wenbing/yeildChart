package com.zwb.yeildchart.view.home;

import android.webkit.WebView;

import com.zwb.yeildchart.R;
import com.zwb.yeildchart.base.BaseActivity;

/**
 * @author Administrator
 * @date 2019/8/15 14:18
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initPageView() {
        setRightVisible(false);
        setTitleText("注意事项");
        WebView webView = $(R.id.webwiew);
        // 加载apk包中的html页面
        webView.loadUrl("file:////android_asset/about.html");
    }

    @Override
    protected void initListener() {

    }
}
