package com.zwb.yeildchart.base;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwb.yeildchart.R;


public abstract class BaseFragment extends Fragment {
	public Context mContext;
	//是否执行了OnAttach
	private boolean isExceOnAttach = false;
	//是否隐藏页面了
	private boolean isHidePage = false;
	//是否正在展示页面
	public boolean isShowingPage = false;
	//基类view
	public View baseMainView;
	//顶部title布局
	public RelativeLayout layoutTitle;
	//界面内容区域布局
	public RelativeLayout layoutContent;
	//数据为空时布局
	public LinearLayout layoutEmpty;
	//数据为空时候的icon
	public ImageView ivEmpty;
	//数据为空时候的文案
	public TextView tvEmpty;
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		isExceOnAttach = true;
		isShowingPage = true;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		baseMainView = inflater.inflate(R.layout.fragment_base,  null);
		mContext = this.getActivity();
		findBaseViewById();
		initPageView();
		initListener();
		initData();
		return baseMainView;
	}

	/**
	 * 获取全局页面控件对象
	 */
	private void findBaseViewById() {
		layoutTitle = $(R.id.layout_title);
		layoutContent = $(R.id.layout_content);
		layoutEmpty = $(R.id.layout_empty);
		ivEmpty = $(R.id.iv_empty);
		tvEmpty = $(R.id.tv_empty);

		View contentView = View.inflate(getActivity(), initPageLayoutID(), null);
		if (contentView == null) {
			layoutContent.setVisibility(View.GONE);
		} else {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
			layoutContent.setVisibility(View.VISIBLE);
			layoutContent.addView(contentView, layoutParams);
		}
	}

	protected <T extends View> T $(int id) {
		return (T) baseMainView.findViewById(id);
	}
	@Override
	public void onResume() {
		super.onResume();
		if (!isExceOnAttach && !isHidePage) {
			// 不是第一次进来并且没有隐藏，需要刷新页面可以实现此方法
			isShowingPage = true;
		}
		if (isShowingPage) {
			onResumePage();
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		isHidePage = hidden;
		isShowingPage = !hidden;
		if (!hidden) {
			onResumePage();
		} else {
			onPausePage();
		}
	}

	/**
	 * 页面暂时关起，留给子fragment重写,默认不做任何处理
	 *
	 * @param params
	 */
	public void onPausePage(Object... params) {

	}
	/**
	 * 获取内容布局
	 */
	protected abstract int initPageLayoutID();

	/**
	 * 重新进入页面逻辑处理，留给子fragment重写,默认不做任何处理
	 * params
	 *
	 * @param
	 */
	public abstract void onResumePage();

	/**
	 * 抽象方法 ，子类必须实现，初始化页面控件。
	 */
	protected abstract void initPageView();

	/**
	 * 抽象方法 ，子类必须实现，页面控件点击事件处理
	 */
	protected abstract void initListener();


	/**
	 * 初始数据
	 */
	public void initData(){

	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
