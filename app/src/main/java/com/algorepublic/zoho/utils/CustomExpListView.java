package com.algorepublic.zoho.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by android on 2/1/16.
 */
public class CustomExpListView extends ExpandableListView {
    public CustomExpListView(Context context)
    {
        super(context);
    }
    public CustomExpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExpListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
