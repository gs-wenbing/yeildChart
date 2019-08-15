package com.zwb.yeildchart.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.zwb.yeildchart.R;

/**
 * @author Administrator
 * @date 2019/8/14 17:12
 */
public class YeildMarkerView extends MarkerView {
    private TextView tvContent;
    private TextView tvTime;
    private String mPreDate;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public YeildMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        tvTime = findViewById(R.id.tvTime);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY());
        tvTime.setText(mPreDate + "-" + ((int) e.getX()));

    }

    private MPPointF mOffset;
    @Override
    public MPPointF getOffset() {
        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());

        }

        return mOffset;
    }
    public static final int ARROW_SIZE = 10; // 箭头的大小（可忽略，根据自己来设置）
    private static final float STOKE_WIDTH = 5;//这里对于stroke_width的宽度也要做一定偏移



    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = getOffset();
        Chart chart = getChartView();
        float width = getWidth();
        float height = getHeight();
// posY \posX 指的是markerView左上角点在图表上面的位置
//处理Y方向
        if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
            offset.y = ARROW_SIZE;
        } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
            offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
        }
//处理X方向，分为3种情况，1、在图表左边 2、在图表中间 3、在图表右边（不考虑超出屏幕问题）
        offset.x = 0;
        if (posX > width / 2) {//如果大于markerView的一半，说明箭头在中间，所以向右偏移一半宽度
            offset.x = -(width / 2);
        }
        return offset;
    }
    public void setPreDate(String preDate) {
        this.mPreDate = preDate;
    }
}
