package com.example.lockscreen;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class LockScreenView extends RelativeLayout {
    private Context mActivityContext = null;
    private View mLayoutView = null;
    public LockScreenView(Context context) {
        super(context);
        mActivityContext = context;
    }

    public LockScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivityContext = context;
    }

    public LockScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivityContext = context;
    }

    @TargetApi(21)
    public LockScreenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mActivityContext = context;
    }

}
