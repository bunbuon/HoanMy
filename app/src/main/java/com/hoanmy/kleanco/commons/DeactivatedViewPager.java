package com.hoanmy.kleanco.commons;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

public class DeactivatedViewPager extends ViewPager {

    public DeactivatedViewPager(Context context) {
        super(context);
    }

    public DeactivatedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}