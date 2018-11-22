package com.example.thasneem.textviewanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputFilter;
import android.util.AttributeSet;

public class AnimateTextView extends AppCompatTextView {
    private static final int DEFAULT_CHAR_LENGTH = 8;

    private static final String PROPERTY_RADIUS = "font";
    private static final String PROPERTY_X = "x_value";
    private static final String PROPERTY_Y = "y_value";
    private ValueAnimator animator;
    private Paint textPaint;
    private Paint textAnimatePaint;

    private int textSize;
    private float textPosY;
    private int textColor;
    private int codeLength;
    private Rect textBounds = new Rect();

    private Typeface fontFamily;
    private float xValue;
    private float animateX;
    private float animateTextPosY;

    public AnimateTextView(Context context) {
        super(context);
        init(null, context);
    }


    public AnimateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public AnimateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        animator = new ValueAnimator();
        initDefaultAttrs();
        initCustomAttrs(context, attrs);
        initPaints();
    }

    private void initPaints() {
        textPaint = new Paint();

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(fontFamily);
        textPaint.setAntiAlias(true);

        textAnimatePaint = new Paint();
        textAnimatePaint.setColor(textColor);
        textAnimatePaint.setTextSize(textSize);
        textAnimatePaint.setTypeface(fontFamily);
        textAnimatePaint.setAntiAlias(true);
    }

    @SuppressLint("ResourceType")
    private void initCustomAttrs(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) return;
        TypedArray attributes = context.obtainStyledAttributes(
                attributeSet, R.styleable.AnimateTextView);
        int fontID = attributes.getResourceId(R.styleable.AnimateTextView_font_family, -1);
        codeLength = attributes.getInt(R.styleable.AnimateTextView_char_length, DEFAULT_CHAR_LENGTH);
        if (fontID != -1) {
            fontFamily = ResourcesCompat.getFont(getContext(), fontID);
        }

        int[] set = {
                android.R.attr.textSize,      // idx 0
                android.R.attr.textColor    // idx 1
        };

        TypedArray a = context.obtainStyledAttributes(attributeSet, set);
        textSize = (int) a.getDimension(0, 10);
        textColor = a.getColor(1, Color.BLACK);


        animator.setDuration(1000);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(codeLength)});

        a.recycle();
        attributes.recycle();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (animator != null)
            animator.start();

    }

    private void initDefaultAttrs() {
        codeLength = DEFAULT_CHAR_LENGTH;
        setCursorVisible(false);
        setBackground(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int size = getPaddingBottom()
                + getPaddingTop()
                + textBounds.height()
                + textSize + 550;
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {
        int size = (getPaddingLeft() + getPaddingRight() + textSize) * codeLength + 500;
        return resolveSizeAndState(size, measureSpec, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureSizes(w, h);
    }


    private void measureSizes(int viewWidth, int viewHeight) {
        if (codeLength <= 0) {
            throw new IllegalArgumentException("Char length must be over than zero");
        }

        float sectionWidth = viewWidth / (codeLength);
        textPosY = viewHeight - 50;
        animateTextPosY = viewHeight - viewHeight / 5;
        int animateInitialX = viewWidth / 2;
        animateX = sectionWidth + sectionWidth / 2 / 2;
        xValue = sectionWidth + sectionWidth / 2 / 2;

        PropertyValuesHolder propertyXValues = PropertyValuesHolder.ofFloat(PROPERTY_X, animateInitialX, animateX);
        PropertyValuesHolder propertyYValues = PropertyValuesHolder.ofFloat(PROPERTY_Y, viewHeight / 2, textPosY);
        PropertyValuesHolder propertyRadius = PropertyValuesHolder.ofInt(PROPERTY_RADIUS, 250, textSize);
        animator.setValues(propertyRadius, propertyXValues, propertyYValues);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textSize = (int) animation.getAnimatedValue(PROPERTY_RADIUS);
                animateX = (float) animation.getAnimatedValue(PROPERTY_X);
                animateTextPosY = (float) animation.getAnimatedValue(PROPERTY_Y);
                textAnimatePaint.setTextSize(textSize);
                invalidate();
            }

        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (getText().length() > 0) {
            canvas.drawText(getText().toString(), 0, getText().length() - 1, xValue, textPosY, textPaint);
        }
        if (getText().length() > 0 && textSize > 47) {
            textPaint.getTextBounds(getText().toString(), 0, getText().length() - 1, textBounds);
            canvas.drawText(getText().charAt(getText().length() - 1) + "", 0, 1, xValue + textBounds.right, animateTextPosY, textAnimatePaint);
        } else {
            canvas.drawText(getText().toString(), 0, getText().length(), xValue, textPosY, textPaint);
        }

    }

    public void setText(String s) {
        super.setText(s);
    }
}
