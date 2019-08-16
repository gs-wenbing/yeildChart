package com.zwb.yeildchart.view.home;


import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.zwb.yeildchart.R;
import com.zwb.yeildchart.base.BaseActivity;
import com.zwb.yeildchart.dialog.TimeDialog;
import com.zwb.yeildchart.model.Team;
import com.zwb.yeildchart.model.Yeild;
import com.zwb.yeildchart.model.Yeild_Table;
import com.zwb.yeildchart.utils.Utils;
import com.zwb.yeildchart.widget.YeildMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG="ChartActivity";
    private Team mTeam;
    TextView tvStartDate,tvEndDate;
    Button btnQuery;
    LineChart lineChart;
    XAxis xAxis;
    YAxis leftAxis;
    YeildMarkerView mMarkerView;
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_chart;
    }

    @Override
    protected void getIntentData() {
        mTeam = getIntent().getParcelableExtra("data");
    }

    @Override
    protected void initPageView() {
        setRightVisible(false);
        setTitleText(mTeam.getTeamName() + "报表");
        tvStartDate = $(R.id.tv_start_date);
        tvEndDate = $(R.id.tv_end_date);
        lineChart = $(R.id.lineChart);
        btnQuery = $(R.id.btn_query);

        mMarkerView =   new YeildMarkerView(mContext,R.layout.content_marker_view);

        Date date = new Date();
        tvStartDate.setText(Utils.getFirstAndLastOfMonth(date)[0]);
        tvEndDate.setText(Utils.getFirstAndLastOfMonth(date)[1]);
        initChart();
        setSingleDatas();
    }

    @Override
    protected void initListener() {
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        List<Yeild> teams = SQLite.select().from(Yeild.class).where(Yeild_Table.teamID.eq(mTeam.getTeamID())).queryList();
        Log.e(TAG, teams.toString());
    }

    /**
     * 设置Chart的一些基本配置
     */
    private void initChart() {
        //配置基本信息
        lineChart.getDescription().setEnabled(false);   //设置描述
        lineChart.setTouchEnabled(true);    //设置是否可以触摸
        lineChart.setDragDecelerationFrictionCoef(0.9f);    //设置滚动时的速度快慢
        lineChart.setDragEnabled(true);     // 是否可以拖拽
        lineChart.setScaleXEnabled(true);   //设置X轴是否能够放大
        lineChart.setScaleYEnabled(false);  //设置Y轴是否能够放大
        //lineChart.setScaleEnabled(true);    // 是否可以缩放 x和y轴, 默认是true
        lineChart.setDrawGridBackground(false);//设置图表内格子背景是否显示，默认是false
        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setBackgroundColor(Color.WHITE);  //设置背景颜色
        lineChart.setMarker(mMarkerView);

        //无数据时显示的文字
        lineChart.setNoDataText("暂无数据");
        lineChart.setNoDataTextColor(R.color.common_tv_color);

        //配置X轴属性
        xAxis = lineChart.getXAxis();
        //折线图不显示数值
        //xAxis.setLabelRotationAngle(25f);  //设置旋转偏移量
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //设置X轴的位置
        //设置标签文本格式
        xAxis.setTextSize(10f);
        //设置标签文本颜色
        xAxis.setTextColor(Color.BLUE);
        //是否绘制轴线
        xAxis.setDrawAxisLine(true);
        //是否绘制网格线
        xAxis.setDrawGridLines(false);
        // 标签倾斜
        xAxis.setLabelRotationAngle(-75);
        //设置是否一个格子显示一条数据，如果不设置这个属性，就会导致X轴数据重复并且错乱的问题
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(8, false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int IValue = (int) value;
                Yeild yeild = SQLite.select().from(Yeild.class).where(Yeild_Table.id.eq(IValue)).querySingle();
                if (yeild != null) {
                    return yeild.getDate();
                }else{
                    return IValue+"";
                }
            }

        });

        //配置Y轴信息
        leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);   //设置Y轴的位置
        leftAxis.setDrawGridLines(true);    //设置左边的网格线显示
        leftAxis.setGranularityEnabled(false);//启用在放大时限制y轴间隔的粒度特性。默认值：false。
        leftAxis.setTextColor(Color.BLUE); //设置Y轴文字颜色

        YAxis rightAxis = lineChart.getAxisRight(); //获取右边的Y轴
        rightAxis.setEnabled(false);   //设置右边的Y轴不显示

        //设置图例（也就是曲线的标签）
        Legend legend = lineChart.getLegend();//设置比例图
        legend.setEnabled(true);   //因为自带的图例太丑，而且操作也不方便，楼主选择自定义，设置不显示比例图
    }

    /**
     * 设置单条折线的数据
     * count 单条折线的数据量
     */
    private void setSingleDatas() { //设置单条折线的X轴数据
        OperatorGroup op = OperatorGroup.clause()
                .and(Yeild_Table.teamID.eq(mTeam.getTeamID()))
                .and(Yeild_Table.date.greaterThanOrEq(tvStartDate.getText().toString()))
                .and(Yeild_Table.date.lessThanOrEq(tvEndDate.getText().toString()));
        List<Yeild> teams = SQLite.select()
                .from(Yeild.class)
                .where(op)
                .orderBy(Yeild_Table.date, true)
                .queryList();
        //设置单条折现的Y轴数据
        ArrayList<Entry> yList = new ArrayList<Entry>();
        for (int i = 0; i < teams.size(); i++) {
            Yeild yeild = teams.get(i);

            yList.add(new Entry(yeild.getId(), yeild.getNumber()));
        }

        LineDataSet lineDataSet = new LineDataSet(yList, mTeam.getTeamName());   //设置单条折线
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        //设置折线的属性
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);   //设置左右两边Y轴节点描述
        lineDataSet.setValueTextColor(Color.BLUE); //设置节点文字颜色
        lineDataSet.setDrawCircles(true);  //设置是否显示节点的小圆点
        lineDataSet.setDrawValues(false);       //设置是否显示节点的值
        lineDataSet.setHighLightColor(Color.RED);//当点击节点时，将会出现与节点水平和垂直的两条线，可以对其进行定制.此方法为设置线条颜色
        lineDataSet.setHighlightEnabled(true);//设置是否显示十字线
        lineDataSet.setColor(ColorTemplate.getHoloBlue());    //设置线条颜色
        lineDataSet.setCircleColor(ColorTemplate.getHoloBlue());  //设置节点的圆圈颜色
        lineDataSet.setLineWidth(1f);   //设置线条宽度
        lineDataSet.setCircleRadius(3f);//设置每个坐标点的圆大小
        lineDataSet.setDrawCircleHole(false);//是否定制节点圆心的颜色，若为false，则节点为单一的同色点，若为true则可以设置节点圆心的颜色
        lineDataSet.setValueTextSize(12f);   //设置 DataSets 数据对象包含的数据的值文本的大小（单位是dp）。
        //设置折线图填充
        lineDataSet.setDrawFilled(false);    //Fill填充，可以将折线图以下部分用颜色填满
        lineDataSet.setFillAlpha(65);       ////设置填充区域透明度，默认值为85
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());//设置填充颜色
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);

        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);    //添加数据
        if(teams.isEmpty()){
            Toast.makeText(mContext, "暂无数据，请选择其他年月试试！", Toast.LENGTH_SHORT).show();
        }
        lineChart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                showDateSelector(tvStartDate);
                break;
            case R.id.tv_end_date:
                showDateSelector(tvEndDate);
                break;
            case R.id.btn_query:
                setSingleDatas();
                break;
            default:
                break;
        }
    }

    private void showDateSelector(TextView tvDate) {

        TimeDialog chd = new TimeDialog(this, new TimeDialog.OnChatHintLinstener() {
            @Override
            public void onConfirm(String year, String month, String day, String hours, String mins) {
                tvDate.setText(year + "-" + month + "-" + day);
            }

            @Override
            public void onCancel() {

            }
        });

        chd.setShowValue(true, false, false);
        if (!TextUtils.isEmpty(tvDate.getText().toString())) {
            String year = tvDate.getText().toString().split("-")[0];
            String month = tvDate.getText().toString().split("-")[1];
            String day = tvDate.getText().toString().split("-")[2];
            chd.setTime(year, month, day, "0");
        }
        chd.show();
    }
}