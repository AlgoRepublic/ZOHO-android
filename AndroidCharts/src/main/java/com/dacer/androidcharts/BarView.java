package com.dacer.androidcharts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

/**
 * Created by Dacer on 11/11/13.
 */
public class BarView extends View {
    private ArrayList<Float> percentList;
    private ArrayList<Float> targetPercentList;
    private Paint textPaint;
    private Paint bgPaint;
    private Paint fgPaint1,fgPaint2,fgPaint3;
    private Rect rect;
    private int barWidth;
//    private boolean showSideMargin = true;
    private int bottomTextDescent;
    private boolean autoSetWidth = true;
    private int topMargin;
    private int bottomTextHeight;
    private ArrayList<String> bottomTextList = new ArrayList<String>();
    private final int MINI_BAR_WIDTH;
    private final int BAR_SIDE_MARGIN;
    private final int TEXT_TOP_MARGIN;
   // private final int TEXT_COLOR = Color.parseColor("#414042");
    private final int BACKGROUND_COLOR = Color.parseColor("#F6F6F6");
    private final int FOREGROUND_COLOR1 = Color.parseColor("#DDF4FD");
    private final int FOREGROUND_COLOR2 = Color.parseColor("#DFEFB4");
    private final int FOREGROUND_COLOR3 = Color.parseColor("#FDEADD");

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
                boolean needNewFrame = false;
                for (int i=0; i<targetPercentList.size();i++) {
                    if (percentList.get(i) < targetPercentList.get(i)) {
                        percentList.set(i,percentList.get(i)+0.02f);
                        needNewFrame = true;
                    } else if (percentList.get(i) > targetPercentList.get(i)){
                        percentList.set(i,percentList.get(i)-0.02f);
                        needNewFrame = true;
                    }
                    if(Math.abs(targetPercentList.get(i)-percentList.get(i))<0.02f){
                        percentList.set(i,targetPercentList.get(i));
                    }
                }
                if (needNewFrame) {
                    postDelayed(this, 20);
                }
                invalidate();
        }
    };

    public BarView(Context context){
        this(context,null);
    }
    public BarView(Context context, AttributeSet attrs){
        super(context, attrs);
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(BACKGROUND_COLOR);

        fgPaint1 = new Paint(bgPaint);
        fgPaint2 = new Paint(bgPaint);
        fgPaint3 = new Paint(bgPaint);

        fgPaint1.setColor(FOREGROUND_COLOR1);
        fgPaint2.setColor(FOREGROUND_COLOR2);
        fgPaint3.setColor(FOREGROUND_COLOR3);

        rect = new Rect();
        topMargin = MyUtils.dip2px(context, 5);
        int textSize = MyUtils.sp2px(context, 18);
        barWidth = MyUtils.dip2px(context,40);
        MINI_BAR_WIDTH = MyUtils.dip2px(context,30);
        BAR_SIDE_MARGIN  = MyUtils.dip2px(context,10);
        TEXT_TOP_MARGIN = MyUtils.dip2px(context, 5);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        percentList = new ArrayList<Float>();
    }

    /**
     * dataList will be reset when called is method.
     * @param bottomStringList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomStringList,int Color){
//        this.dataList = null;
        textPaint.setColor(Color);
        this.bottomTextList = bottomStringList;
        Rect r = new Rect();
        bottomTextDescent = 0;
        barWidth = MINI_BAR_WIDTH;
        for(String s:bottomTextList){
            textPaint.getTextBounds(s,0,s.length(),r);
            if(bottomTextHeight<r.height()){
                bottomTextHeight = r.height();
            }
            if(autoSetWidth&&(barWidth<r.width())){
                barWidth = r.width();
            }
            if(bottomTextDescent<(Math.abs(r.bottom))){
                bottomTextDescent = Math.abs(r.bottom);
            }
        }
        setMinimumWidth(2);
        postInvalidate();
    }
    /**
     *
     * @param list The ArrayList of Integer with the range of [0-max].
     */
    public void setDataList(ArrayList<Integer> list, int max){
        targetPercentList = new ArrayList<Float>();
        if(max == 0) max = 1;

        for(Integer integer : list){
            targetPercentList.add(1-(float)integer/(float)max);
        }

        // Make sure percentList.size() == targetPercentList.size()
        if(percentList.isEmpty() || percentList.size()<targetPercentList.size()){
            int temp = targetPercentList.size()-percentList.size();
            for(int i=0; i<temp;i++){
                percentList.add(1f);
            }
        } else if (percentList.size()>targetPercentList.size()){
            int temp = percentList.size()-targetPercentList.size();
            for(int i=0; i<temp;i++){
                percentList.remove(percentList.size()-1);
            }
        }
        setMinimumWidth(2);
        removeCallbacks(animator);
        post(animator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int i = 1;
        if(percentList != null && !percentList.isEmpty()){
            for(Float f:percentList){
                rect.set(BAR_SIDE_MARGIN*i+barWidth*(i-1),
                        topMargin,
                        (BAR_SIDE_MARGIN+barWidth)* i,
                        getHeight()-bottomTextHeight-TEXT_TOP_MARGIN);
                canvas.drawRect(rect,bgPaint);
                /*rect.set(BAR_SIDE_MARGIN*i+barWidth*(i-1),
                        topMargin+(int)((getHeight()-topMargin)*percentList.get(i-1)),
                        (BAR_SIDE_MARGIN+barWidth)* i,
                        getHeight()-bottomTextHeight-TEXT_TOP_MARGIN);*/
				/**
				  * The correct total height is "getHeight()-topMargin-bottomTextHeight-TEXT_TOP_MARGIN",not "getHeight()-topMargin".
				  * fix by zhenghuiy@gmail.com on 11/11/13.
				  */
				rect.set(BAR_SIDE_MARGIN * i + barWidth * (i - 1),
                        topMargin + (int) ((getHeight() - topMargin - bottomTextHeight - TEXT_TOP_MARGIN) * percentList.get(i - 1)),
                        (BAR_SIDE_MARGIN + barWidth) * i,
                        getHeight() - bottomTextHeight - TEXT_TOP_MARGIN);

                if(i==1)
                canvas.drawRect(rect,fgPaint1);
                if(i==2)
                canvas.drawRect(rect, fgPaint2);
                if(i==3)
                canvas.drawRect(rect,fgPaint3);
                i++;
            }
        }

        if(bottomTextList != null && !bottomTextList.isEmpty()){
            i = 1;
            for(int loop=0;loop<bottomTextList.size();loop++){
                canvas.drawText(bottomTextList.get(loop), BAR_SIDE_MARGIN * i + barWidth * (i - 1) + barWidth / 2,
                        getHeight()-bottomTextDescent, textPaint);
                canvas.drawText(bottomTextList.get(++loop), BAR_SIDE_MARGIN * i + barWidth * (i - 1) + barWidth / 2,
                        getHeight() / 2, textPaint);
                i++;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        int mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureWidth(int measureSpec){
        int preferred = 0;
        if(bottomTextList != null){
            preferred = bottomTextList.size()*(barWidth+BAR_SIDE_MARGIN);
        }
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec){
        int preferred = 222;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
    
}
