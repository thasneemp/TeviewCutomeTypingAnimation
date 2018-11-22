package com.example.thasneem.textviewanimation;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * File Description
 * ------------------
 * Author : thasneem
 * Email : thasneem@farabi.ae
 * Date : 11/21/2018
 * Project : android_text_animation
 * Company : Farabi Technology
 */
public class AnimatedTextView extends AppCompatTextView {
    private static final String PROPERTY_RADIUS = "font";
    private static final String PROPERTY_X = "x_value";
    private ValueAnimator animator;
    private int radius;
    private int rotate;
    private Paint backgroundPaint = new Paint();
    private Paint backgroundConstant = new Paint();
    private int fontSize = 50;
    private int index = 0;
    String word = "HelloWorld";
    private String c = "";
    private int font;
    private int xValue;
    private String totalString;
    private static final int DEFAULT_CODE_LENGTH = 4;
    private static final String DEFAULT_CODE_MASK = "*";
    private static final String DEFAULT_CODE_SYMBOL = "0";
    private static final String DEFAULT_REGEX = "[^0-9]";
    private static final float DEFAULT_REDUCTION_SCALE = 0.5f;


    private Paint textPaint;
    private Paint underlinePaint;
    private Paint cursorPaint;

    private float textSize;
    private float textPosY;
    private int textColor;
    private float sectionWidth;
    private int codeLength = 10;
    private float symbolWidth;
    private float symbolMaskedWidth;
    private float underlineHorizontalPadding;
    private float underlineReductionScale;
    private float underlineStrokeWidth;
    private int underlineBaseColor;
    private int underlineSelectedColor;
    private int underlineFilledColor;
    private int underlineCursorColor;
    private float underlinePosY;
    private int fontStyle;
    private boolean cursorEnabled;
    private boolean codeHiddenMode;
    private boolean isSelected;
    private String codeHiddenMask;
    private Rect textBounds = new Rect();

    public AnimatedTextView(Context context) {
        super(context);
        init();
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setText(String text, CharSequence s) {
        totalString = s.toString();
        c = text;
        fontSize = 50;
        animator.start();
        super.setText(text, BufferType.NORMAL);

    }

    private void init() {
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, fontStyle));
        textPaint.setAntiAlias(true);

        underlinePaint = new Paint();
        underlinePaint.setColor(underlineBaseColor);
        underlinePaint.setStrokeWidth(underlineStrokeWidth);

        cursorPaint = new Paint();
        cursorPaint.setColor(underlineBaseColor);
        cursorPaint.setStrokeWidth(underlineStrokeWidth);

        index = 0;
        backgroundPaint.setColor(Color.GREEN);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setTextSize(fontSize);


        backgroundConstant.setColor(Color.GREEN);
        backgroundConstant.setStyle(Paint.Style.FILL);
        backgroundConstant.setTextSize(100);
        int viewWidth = getWidth() / 2;
        int leftTopX = viewWidth - 150;
        PropertyValuesHolder propertyRadius = PropertyValuesHolder.ofInt(PROPERTY_RADIUS, 500, 100);
        PropertyValuesHolder propertyXValue = PropertyValuesHolder.ofInt(PROPERTY_X, leftTopX, 500);
        animator = new ValueAnimator();
        animator.setValues(propertyRadius, propertyXValue);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                font = (int) animation.getAnimatedValue(PROPERTY_RADIUS);
                xValue = (int) animation.getAnimatedValue(PROPERTY_X);
                backgroundPaint.setTextSize(font);
                invalidate();
            }

        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (c.equalsIgnoreCase("")) {
            return;
        }

        int viewWidth = getWidth() / 2;
        int viewHeight = getHeight() / 2;

        int leftTopX = viewWidth - 150;
        int leftTopY = viewHeight - 150;

        int rightBotX = viewWidth + 150;
        int rightBotY = viewHeight + 150;


//        String substring = c.substring(index, index + 1);
//        canvas.drawRoundRect(leftTopX, leftTopY, rightBotX, rightBotY, radius, radius, backgroundPaint);
//        float sectionWidth = getWidth() / totalString.length();
//
//        canvas.drawText(c, 0, 1, xValue, leftTopY, backgroundPaint);
//
//        for (int i = 0; i < totalString.length(); i++) {
//            char[] symbol = {totalString.charAt(i)};
//            float textPosX = i + xValue;
//            canvas.drawText(symbol, 0, 1, textPosX, leftTopY, backgroundConstant);
//        }
//
////        canvas.drawText(totalString, 0, totalString.length() - 1, 500, leftTopY, backgroundConstant);
//
////        if (c.length() > index + 1) {
////            index++;
////        }
        drawText(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureSizes(w, h);
    }

    private void measureSizes(int viewWidth, int viewHeight) {
        if (underlineReductionScale > 1) underlineReductionScale = 1;
        if (underlineReductionScale < 0) underlineReductionScale = 0;

        if (codeLength <= 0) {
            throw new IllegalArgumentException("Code length must be over than zero");
        }

        symbolWidth = textPaint.measureText(DEFAULT_CODE_SYMBOL);
        symbolMaskedWidth = textPaint.measureText("*");
        textPaint.getTextBounds(DEFAULT_CODE_SYMBOL, 0, 1, textBounds);
        sectionWidth = viewWidth / codeLength;
        underlinePosY = viewHeight - getPaddingBottom();
        underlineHorizontalPadding = sectionWidth * underlineReductionScale / 2;
        textPosY = viewHeight / 2 + textBounds.height() / 2;
    }

    private int measureHeight(int measureSpec) {
        int size = (int) (getPaddingBottom()
                + getPaddingTop()
                + textBounds.height()
                + textSize
                + underlineStrokeWidth);
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {
        int size = (int) ((getPaddingLeft() + getPaddingRight() + textSize) * codeLength * 2);
        return resolveSizeAndState(size, measureSpec, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < totalString.length(); i++) {
            char[] symbol = {totalString.charAt(i)};
            float textPosX = sectionWidth * i + sectionWidth / 2 - symbolWidth / 2;
            canvas.drawText(symbol, 0, 1, textPosX, textPosY, textPaint);
        }
    }

}
