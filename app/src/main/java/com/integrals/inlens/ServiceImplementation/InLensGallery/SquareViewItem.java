package com.integrals.inlens.ServiceImplementation.InLensGallery;

import android.content.Context;
import android.util.AttributeSet;

public class SquareViewItem extends android.support.v7.widget.AppCompatImageView {

    public SquareViewItem(Context context) {
        super(context);
    }

    public SquareViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}