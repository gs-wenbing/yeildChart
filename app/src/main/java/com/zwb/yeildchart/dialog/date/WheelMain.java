package com.zwb.yeildchart.dialog.date;

import android.view.View;


import com.zwb.yeildchart.R;

import java.util.Arrays;
import java.util.List;


public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    public int screenheight;
    private boolean hasSelectTime, hasSelectYear, hasSelectMonth, hasSelectDay, hasHalf;
    private static int START_YEAR = 1990, END_YEAR = 2100;
    private boolean hasLabel = true;//年月的数字后面是否需要文字

    int mBackgroundResId = R.drawable.wheel_bg;
    int mForegroundResId = R.drawable.wheel_val;
    /**
     * Current value & label text color
     */
    private int mValueTextColor = 0xFFffffff;

    /**
     * Items text color
     */
    private int mItemsTextColor = R.color.common_tv_color;

    private int mExtraHeight = 0;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static int getSTART_YEAR() {
        return START_YEAR;
    }

    public static void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public static int getEND_YEAR() {
        return END_YEAR;
    }

    public static void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    public WheelMain(View view) {
        super();
        this.view = view;
        hasSelectTime = false;
        setView(view);
    }

    public WheelMain(View view, boolean hasSelectYear, boolean hasSelectMonth, boolean hasSelectDay, boolean hasSelectTime, boolean hasHalf) {
        super();
        this.view = view;
        this.hasSelectYear = hasSelectYear;
        this.hasSelectMonth = hasSelectMonth;
        this.hasSelectDay = hasSelectDay;
        this.hasSelectTime = hasSelectTime;
        this.hasHalf = hasHalf;
        setView(view);
    }

    public WheelMain(View view, boolean hasSelectYear,
                     boolean hasSelectMonth, boolean hasSelectDay,
                     boolean hasSelectTime, boolean hasHalf, boolean hasLabel) {
        super();
        this.view = view;
        this.hasSelectYear = hasSelectYear;
        this.hasSelectMonth = hasSelectMonth;
        this.hasSelectDay = hasSelectDay;
        this.hasSelectTime = hasSelectTime;
        this.hasHalf = hasHalf;
        this.hasLabel = hasLabel;
        setView(view);
    }

    /**
     * 设置背景色
     */
    public void setBackgroundColor(int backgroundResId) {
        mBackgroundResId = backgroundResId;
    }

    /**
     * 设置背景色
     */
    public void setForegroundColor(int foregroundResId) {
        mForegroundResId = foregroundResId;
    }

    /**
     * 为选中的字体颜色
     *
     * @param itemsTextColor
     */
    public void setItemsTextColor(int itemsTextColor) {
        this.mItemsTextColor = itemsTextColor;
    }

    /**
     * 选中字体颜色
     *
     * @param valueTextColor
     */
    public void setValueTextColor(int valueTextColor) {
        this.mValueTextColor = valueTextColor;
    }

    public void setValueExtraHeight(int height) {
        this.mExtraHeight = height;
    }

    public void initDateTimePicker(int year, int month, int day) {
        this.initDateTimePicker(year, month, day, 0, 0);
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void initDateTimePicker(int year, int month, int day, int h, int m) {
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = calendar.get(Calendar.DATE);
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        if (!hasSelectTime && !hasSelectMonth && !hasSelectDay && !hasHalf) {
            if (hasLabel) {//显示文字  “年”
                wv_year.setLabel("年     ");
            } else {
                wv_year.setLabel("");
            }
        } else {
            if (hasLabel)
                wv_year.setLabel("年");// 添加文字
            else
                wv_year.setLabel("");// 不添加文字
        }

        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        if (hasSelectYear) {
            wv_year.setVisibility(View.VISIBLE);
        } else {
            wv_year.setVisibility(View.GONE);
        }
        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        if (hasLabel) {
            wv_month.setLabel("月");
        } else {
            wv_month.setLabel("");
        }
        wv_month.setCurrentItem(month);
        if (hasSelectMonth) {
            wv_month.setVisibility(View.VISIBLE);
        } else {
            wv_month.setVisibility(View.GONE);
        }

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(day - 1);
        if (hasSelectDay) {
            wv_day.setVisibility(View.VISIBLE);
        } else {
            wv_day.setVisibility(View.GONE);
        }


        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);
        if (hasSelectTime) {
            wv_hours.setVisibility(View.VISIBLE);
            wv_mins.setVisibility(View.VISIBLE);

            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCyclic(true);// 可循环滚动
            wv_hours.setLabel("时");// 添加文字
            wv_hours.setCurrentItem(h);

            if (hasHalf) {
                wv_mins.setAdapter(new HalfWheelAdapter(0, 99));
                wv_mins.setVisibleItems(3);
                wv_mins.setCurrentItem(m == 30 ? 1 : 0);
            } else {
                //暂未验证
                wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
                wv_mins.setVisibleItems(5);
                wv_mins.setCurrentItem(m);
            }
            wv_mins.setCyclic(true);// 可循环滚动
            wv_mins.setLabel("分");// 添加文字
        } else {
            wv_hours.setVisibility(View.GONE);
            wv_mins.setVisibility(View.GONE);
        }
        initWheelView();
        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    if (wv_day.getCurrentItem() >= 31) {
                        wv_day.setCurrentItem(30);
                    }
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    if (wv_day.getCurrentItem() >= 30) {
                        wv_day.setCurrentItem(29);
                    }
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        if (wv_day.getCurrentItem() >= 29) {
                            wv_day.setCurrentItem(28);
                        }
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        if (wv_day.getCurrentItem() >= 28) {
                            wv_day.setCurrentItem(27);
                        }
                    }
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    if (wv_day.getCurrentItem() >= 31) {
                        wv_day.setCurrentItem(30);
                    }
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    if (wv_day.getCurrentItem() >= 30) {
                        wv_day.setCurrentItem(29);
                    }
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        if (wv_day.getCurrentItem() >= 29) {
                            wv_day.setCurrentItem(28);
                        }
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        if (wv_day.getCurrentItem() >= 28) {
                            wv_day.setCurrentItem(27);
                        }
                    }
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenheight / 100) * 3;
        else
            textSize = (screenheight / 100) * 4;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

    }

    private void initWheelView() {
        wv_year.setBackgroudColor(mBackgroundResId);
        wv_year.setForegroundColor(mForegroundResId);
        wv_month.setBackgroudColor(mBackgroundResId);
        wv_month.setForegroundColor(mForegroundResId);
        wv_day.setBackgroudColor(mBackgroundResId);
        wv_day.setForegroundColor(mForegroundResId);
        wv_hours.setBackgroudColor(mBackgroundResId);
        wv_hours.setForegroundColor(mForegroundResId);
        wv_mins.setBackgroudColor(mBackgroundResId);
        wv_mins.setForegroundColor(mForegroundResId);

        wv_year.setItemsTextColor(mItemsTextColor);
        wv_year.setValueTextColor(mValueTextColor);
        wv_month.setItemsTextColor(mItemsTextColor);
        wv_month.setValueTextColor(mValueTextColor);
        wv_day.setItemsTextColor(mItemsTextColor);
        wv_day.setValueTextColor(mValueTextColor);
        wv_hours.setItemsTextColor(mItemsTextColor);
        wv_hours.setValueTextColor(mValueTextColor);
        wv_mins.setItemsTextColor(mItemsTextColor);
        wv_mins.setValueTextColor(mValueTextColor);

        wv_year.setValueExtraHeight(mExtraHeight);
        wv_month.setValueExtraHeight(mExtraHeight);
        wv_day.setValueExtraHeight(mExtraHeight);
        wv_hours.setValueExtraHeight(mExtraHeight);
        wv_mins.setValueExtraHeight(mExtraHeight);

    }

    public String getYear() {
        return "" + (wv_year.getCurrentItem() + START_YEAR);
    }

    public String getMonth() {
        if ((wv_month.getCurrentItem() + 1) < 10) {
            return "0" + (wv_month.getCurrentItem() + 1);
        } else {
            return "" + (wv_month.getCurrentItem() + 1);
        }
    }

    public String getDay() {
        if ((wv_day.getCurrentItem() + 1) < 10) {
            return "0" + (wv_day.getCurrentItem() + 1);
        } else {
            return "" + (wv_day.getCurrentItem() + 1);
        }
    }

    public String getHours() {
        if ((wv_hours.getCurrentItem()) < 10) {
            return "0" + (wv_hours.getCurrentItem());
        } else {
            return "" + (wv_hours.getCurrentItem());
        }
    }

    public String getMins() {
        if (hasHalf)
            return (wv_mins.getCurrentItem() % 2 == 0 ? "00" : "30");
        else {
            if (wv_mins.getCurrentItem() < 10) {
                return "0" + (wv_mins.getCurrentItem());
            } else {
                return "" + (wv_mins.getCurrentItem());
            }
        }
    }

//    public String getTime() {
//        StringBuffer sb = new StringBuffer();
////        hasSelectYear, hasHalf
//        if (!hasSelectTime) {
//            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
//                    .append((wv_month.getCurrentItem() + 1)).append("-")
//                    .append((wv_day.getCurrentItem() + 1));
//        } else {
//            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
//                    .append((wv_month.getCurrentItem() + 1)).append("-")
//                    .append((wv_day.getCurrentItem() + 1)).append(" ")
//                    .append(wv_hours.getCurrentItem()).append(":")
//                    .append(wv_mins.getCurrentItem());
//        }
//        return sb.toString();
//    }
}
