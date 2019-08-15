package com.zwb.yeildchart.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwb.yeildchart.R;
import com.zwb.yeildchart.base.adapter.BaseRVAdapter;
import com.zwb.yeildchart.utils.NetWorkUtil;
import com.zwb.yeildchart.widget.LoadingLayout;

import java.util.List;

import static com.zwb.yeildchart.base.BaseActivity.ACTION_DEFAULT;
import static com.zwb.yeildchart.base.BaseActivity.ACTION_LOAD_FAILED;
import static com.zwb.yeildchart.base.BaseActivity.ACTION_LOAD_MORE;
import static com.zwb.yeildchart.base.BaseActivity.ACTION_REFRESH;


/**
 * RecyclerViewFragment
 * @author  zwb
 * create at 2017/8/31 13:15
 */

public class RecyclerFragment<T> extends Fragment {
    protected XRecyclerView mRecyclerView;
    private LoadingLayout rootView;
    protected BaseRVAdapter<T> mAdapter;
    private int mCurrentPage = 1;
    public  int p_pageSize = 15;
    private RecyclerListener recyclerListener;

    public static RecyclerFragment newInstance() {
        Bundle args = new Bundle();
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.fragment_recyclerview_base, null);
        rootView = (LoadingLayout) mContentView.findViewById(R.id.root_view);
        mRecyclerView = (XRecyclerView) mContentView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (recyclerListener != null) {
                    mRecyclerView.reset();
                    recyclerListener.loadData(ACTION_REFRESH, p_pageSize, mCurrentPage = 1);
                }
            }

            @Override
            public void onLoadMore() {
                if (recyclerListener != null) {
                    recyclerListener.loadData(ACTION_LOAD_MORE, p_pageSize, mCurrentPage = mCurrentPage + 1);
                }

            }
        });
        rootView.setReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.reset();
                initData();
            }
        });

        if (recyclerListener != null) {
            recyclerListener.onRecyclerCreated(mRecyclerView);
        }
        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
            initData();
        } else {
            Log.e("RecyclerFragment", "Please call init in activity's onCreate()");
        }
        return mContentView;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (recyclerListener != null) {
//            recyclerListener.onRecyclerCreated(mRecyclerView);
//        }
//    }

    public void init(BaseRVAdapter<T> adapter, RecyclerListener recyclerListener) {
        mAdapter = adapter;
        this.recyclerListener = recyclerListener;
    }

    public boolean needCheckNet = true;

    public void setNeedCheckNet(boolean needCheckNet) {
        this.needCheckNet = needCheckNet;
    }

    public void initData() {
        if (needCheckNet && !NetWorkUtil.isNetworkAvailable(getActivity())) {
            rootView.showFailed("网络不好哦，请稍后再试");
            return;
        }
        rootView.showLoading("数据加载中...");
        if (recyclerListener != null) {
            recyclerListener.loadData(ACTION_DEFAULT, p_pageSize, mCurrentPage = 1);
        }
    }

    public void setLoadingEnable(boolean enabled) {
        if (mRecyclerView != null){
            mRecyclerView.setLoadingMoreEnabled(enabled);
        }
    }

    public void setRefreshEnable(boolean enabled) {
        if (mRecyclerView != null){
            mRecyclerView.setPullRefreshEnabled(enabled);
        }
    }

    public void loadCompleted(int action, String msg, List<T> list) {
        if (getActivity() == null) {
            return;
        }
        if (list != null) {
            if (action == ACTION_LOAD_MORE) {
                //加载更多
                mAdapter.addAll(list, false);
                if (list.size() < p_pageSize) {
                    //加载的数据少于一页时
                    mRecyclerView.setNoMore(false);
                }
            } else {
                //首次加载、下拉刷新
                mAdapter.addAll(list, true);
            }
        }
        if (action == ACTION_LOAD_FAILED) {
            rootView.showFailed("网络不好哦，请稍后再试");
        } else {
            if (mAdapter.getBeans().size() == 0) {
                rootView.showEmpty();
            } else {
                rootView.showContent();
            }
        }
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();
    }


    public void setP_pageSize(int p_pageSize) {
        this.p_pageSize = p_pageSize;
    }

    public interface RecyclerListener {
        /**
         * recyclerView创建完成
         * @param recyclerView 当前创建的recyclerView
         */
        void onRecyclerCreated(XRecyclerView recyclerView);

        /**
         * 分页加载数据
         * @param action 当前加载的动作
         * @param pageSize 每页数
         * @param page 第几页
         */
        void loadData(int action, int pageSize, int page);
    }
}
