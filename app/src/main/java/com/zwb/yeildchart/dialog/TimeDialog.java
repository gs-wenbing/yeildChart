package com.zwb.yeildchart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.zwb.yeildchart.R;
import com.zwb.yeildchart.dialog.date.JudgeDate;
import com.zwb.yeildchart.dialog.date.ScreenInfo;
import com.zwb.yeildchart.dialog.date.WheelMain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @desc 日期选择器
 * @auther zwb
 * create at 2017/3/17 17:24
 */

public class TimeDialog extends Dialog implements View.OnClickListener {
    private OnChatHintLinstener mOnChatHintLinstener = null;
    private Window window;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean hasTime = false;//是否显示时分
    private boolean hasYear = true;//是否显示年
    private boolean hasMonth = true;//是否显示月
    private boolean hasDay = true;//是否显示年
    private boolean hasHalf = true;//分显示成00 和 30
    WheelMain wheelMain;
    private int year = 0 ;//当前的年月日时分
    private int month = 0 ;
    private int day = 0 ;
    private int hour = 0 ;
    private int min = 0 ;


    public void setTime(String year, String month, String day, String hour) {
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month)-1;
        this.day = Integer.parseInt(day);
        this.hour = Integer.parseInt(hour);
    }

    public void setTime(String year, String month, String day, String hour, String min) {
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month)-1;
        this.day = Integer.parseInt(day);
        this.hour = Integer.parseInt(hour);
        this.min = Integer.parseInt(min);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm://确定
                mOnChatHintLinstener.onConfirm(wheelMain.getYear(), wheelMain.getMonth(), wheelMain.getDay(), wheelMain.getHours(), wheelMain.getMins());
                dismiss();
                break;
            case R.id.tv_cencal://取消
                mOnChatHintLinstener.onCancel();
                dismiss();
                break;
        }
    }

    public interface OnChatHintLinstener {
        /**
         * @param year  年
         * @param month 月
         * @param day   日
         * @param hours 时
         * @param mins  分
         */
        void onConfirm(String year, String month, String day, String hours, String mins);

        void onCancel();
    }

    Context mContext;

    public TimeDialog(Context context, OnChatHintLinstener mOnChatHintLinstener) {
        super(context, R.style.dialog);
        this.mOnChatHintLinstener = mOnChatHintLinstener;
        init(context);
    }

    public TimeDialog(Context context, int theme, OnChatHintLinstener mOnChatHintLinstener) {
        super(context, R.style.dialog);
        this.mOnChatHintLinstener = mOnChatHintLinstener;
        init(context);
    }

    protected TimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener, OnChatHintLinstener mOnChatHintLinstener) {
        super(context, cancelable, cancelListener);
        this.mOnChatHintLinstener = mOnChatHintLinstener;
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.SCREEN_ORIENTATION_CHANGED,
                WindowManager.LayoutParams.SCREEN_ORIENTATION_CHANGED);
        window = getWindow();
        window.setContentView(R.layout.dialog_time);
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.75

        window.setAttributes(lp);
        window.findViewById(R.id.tv_confirm).setOnClickListener(this);
        window.findViewById(R.id.tv_cencal).setOnClickListener(this);
    }

    /**
     * @param hasYear 是否显示年
     * @param hasTime 是否显示时、分
     * @param hasHalf 分是否显示成  00 30
     */
    public void setShowValue(boolean hasYear, boolean hasTime, boolean hasHalf) {
        this.hasYear = hasYear;
        this.hasTime = hasTime;
        this.hasHalf = hasHalf;
    }

    /**
     * @param hasYear 是否显示年
     * @param hasDay  是否显示日
     * @param hasTime 是否显示时、分
     * @param hasHalf 分是否显示成  00 30
     */
    public void setShowValue(boolean hasYear, boolean hasDay, boolean hasTime, boolean hasHalf) {
        this.hasYear = hasYear;
        this.hasDay = hasDay;
        this.hasTime = hasTime;
        this.hasHalf = hasHalf;
    }

    /**
     * @param hasYear 是否显示年
     * @param hasMonth 是否显示月
     * @param hasDay 是否显示日
     * @param hasTime 是否显示时、分
     * @param hasHalf 分是否显示成  00 30
     */
    public void setShowValue(boolean hasYear, boolean hasMonth,boolean hasDay, boolean hasTime, boolean hasHalf) {
        this.hasYear = hasYear;
        this.hasMonth = hasMonth;
        this.hasDay = hasDay;
        this.hasTime = hasTime;
        this.hasHalf = hasHalf;
    }


    @Override
    public void show() {
        super.show();
        final View timepickerview = window.findViewById(R.id.timePicker1);
        initWheelMain(timepickerview);
    }

    /**
     * 初始化日期选择器
     *
     * @param timepickerview
     */
    private void initWheelMain(View timepickerview) {

        Calendar calendar = Calendar.getInstance();
        ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
        wheelMain = new WheelMain(timepickerview, hasYear, hasMonth, hasDay, hasTime, hasHalf);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";

        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (year == 0 && month == 0) {//判断是否有时间，没有的话时间选择器就用当前的时间
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
        }


        if (hasTime)
            wheelMain.initDateTimePicker(year, month, day, hour, min);
        else
            wheelMain.initDateTimePicker(year, month, day);
    }

}
