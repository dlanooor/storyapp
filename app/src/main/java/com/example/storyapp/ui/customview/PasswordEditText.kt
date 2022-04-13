package com.example.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText : AppCompatEditText, View.OnTouchListener {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "******"
        transformationMethod = PasswordTransformationMethod.getInstance()
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        maxLines = 1
    }

    private fun init() {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }

}