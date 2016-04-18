package com.algorepublic.zoho.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;


public class CustomExpListView extends ExpandableListView
{
    public CustomExpListView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }
    public CustomExpListView(Context context)
    {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000000000, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}