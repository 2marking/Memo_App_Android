package com.example.appchallenge_lineplus_android.ImagePicker.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class SquareItemLayout : RelativeLayout {
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(context: Context?) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec))

        val childWidthSize = measuredWidth
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        heightMeasureSpec = widthMeasureSpec

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}