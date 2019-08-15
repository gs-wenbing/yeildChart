package com.zwb.yeildchart.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author zwb
 * create at 2017/8/1 10:40
 * @desc RecyclerViewAdapter
 */

public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseRVAdapter.CommonViewHolder> {

    protected List<T> mBeans;
    protected Context mContext;
    protected boolean mAnimateItems = false;
    protected int mLastAnimatedPosition = -1;

    public BaseRVAdapter(Context context, List<T> beans) {
        mContext = context;
        mBeans = beans;
    }

    @Override
    public BaseRVAdapter.CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(getItemLayoutID(viewType), parent, false);
        CommonViewHolder holder = new CommonViewHolder(view);
        return holder;
    }

    /**
     * 取得ItemView的布局文件
     *
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutID(int viewType);


    @Override
    public void onBindViewHolder(BaseRVAdapter.CommonViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        onBindDataToView(holder, mBeans.get(position), position);
    }

    /**
     * 绑定数据到Item的控件中去
     *
     * @param holder
     * @param bean
     * @param position
     */
    protected abstract void onBindDataToView(CommonViewHolder holder, T bean, int position);


    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    public T getItem(int position) {
        return mBeans.get(position);
    }

    public void add(T bean) {
        mBeans.add(bean);
        notifyDataSetChanged();
    }

    public void addAll(List<T> beans) {
        addAll(beans, false);
    }

    public void addAll(int position, List<T> beans) {
        mBeans.addAll(position, beans);
        notifyDataSetChanged();
    }

    public void addAll(List<T> beans, boolean clearPrevious) {
        if (clearPrevious) {
            mBeans = beans;
            notifyDataSetChanged();
        } else {
            mBeans.addAll(beans);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mBeans.clear();
        notifyDataSetChanged();
    }

    public List<T> getBeans() {
        return mBeans;
    }

    public boolean isFinishing() {
        return mContext == null || mContext instanceof Activity && ((Activity) mContext).isFinishing();
    }

    /***
     * item的加载动画
     *
     * @param view
     * @param position
     */
    private void runEnterAnimation(final View view, int position) {
//        if (!mAnimateItems) {
//            return;
//        }
//        if (position > mLastAnimatedPosition) {
//            mLastAnimatedPosition = position;
//            view.setAlpha(0);
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Animation animation = AnimationUtils.loadAnimation(view.getContext(),
//                            R.anim.slide_in_right);
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                            view.setAlpha(1);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//                        }
//                    });
//                    view.startAnimation(animation);
//                }
//            }, 2000);
//        }
    }


    public class CommonViewHolder extends RecyclerView.ViewHolder {
        private final SparseArray<View> mViews;
        public View itemView;

        public CommonViewHolder(View itemView) {
            super(itemView);
            this.mViews = new SparseArray<>();
            this.itemView = itemView;
            //添加Item的点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLinsener != null) {
                        mOnItemClickLinsener.onItemClick(BaseRVAdapter.this, getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickLinsener != null) {
                        mOnItemLongClickLinsener.onItemLongClick(BaseRVAdapter.this, getAdapterPosition());
                    }
                    return false;
                }
            });
        }


        public <T extends View> T $(int viewId) {

            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                if (view == null) {
                    return null;
                }
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, CharSequence text) {
            TextView tv = $(viewId);
            tv.setText(text);
        }

        /**
         * 加载drawable中的图片
         *
         * @param viewId
         * @param resId
         */
        public void setImageResource(int viewId, int resId) {
            ImageView iv = $(viewId);
            iv.setImageResource(resId);
        }

        /**
         * 加载网络上的图片
         *
         * @param viewId
         * @param url
         */
        public void setImageResource(int viewId, String url) {
            ImageView iv = $(viewId);
//            Glide.with(mContext).load(url).error(R.drawable.defualt_pictures_no).into(iv);
        }
    }

    OnItemClickLinsener mOnItemClickLinsener;

    public void setOnItemClickLinsener(OnItemClickLinsener linsener) {
        mOnItemClickLinsener = linsener;
    }

    OnItemLongClickLinsener mOnItemLongClickLinsener;

    public void setOnItemLongClickLinsener(OnItemLongClickLinsener linsener) {
        mOnItemLongClickLinsener = linsener;
    }

    /**
     * XRecyclerView的Item的点击事件
     */
    public interface OnItemClickLinsener {
        /**
         * onItemClick
         *
         * @param baseAdapter
         * @param position
         */
        void onItemClick(BaseRVAdapter baseAdapter, int position);
    }

    /**
     * XRecyclerView的Item的点击事件
     */
    public interface OnItemLongClickLinsener {
        /**
         * onItemClick
         *
         * @param baseAdapter
         * @param position
         */
        void onItemLongClick(BaseRVAdapter baseAdapter, int position);
    }
}
