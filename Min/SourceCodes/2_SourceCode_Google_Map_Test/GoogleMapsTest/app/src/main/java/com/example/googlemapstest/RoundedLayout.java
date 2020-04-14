package com.example.googlemapstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class RoundedLayout  extends RelativeLayout {
    private Path mPathCorners = new Path();
    private Path mPathCircle = new Path();
    private float mCornerRadius;

    /**
     * border path
     */
    private Path mPathCornersBorder = new Path();
    private Path mPathCircleBorder = new Path();
    private int mBorderWidth = 0;
    private int mBorderHalf;
    private boolean mShowBorder = false;
    private int mBorderColor = 0xFFFF7700;

    private float mDensity = 1.0f;

    /**
     * Rounded corners or circle shape
     */
    private boolean mIsCircleShape = false;

    private Paint mPaint = new Paint();

    private float dpFromPx(final float px) {
        return px / mDensity;
    }

    private float pxFromDp(final float dp) {
        return dp * mDensity;
    }

    public RoundedLayout(Context context) {
        this(context, null);
    }

    public RoundedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDensity = getResources().getDisplayMetrics().density;
        // just a default for corner radius
        mCornerRadius = pxFromDp(25f);

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mBorderColor);
        setBorderWidth(Math.round(pxFromDp(2f)));
    }

    /**
     * Switch to circle or rectangle shape
     *
     * @param useCircle
     */
    public void setShapeCircle(boolean useCircle) {
        mIsCircleShape = useCircle;
        invalidate();
    }

    /**
     * change corner radius
     *
     * @param radius
     */
    public void setCornerRadius(int radius) {
        mCornerRadius = radius;
        invalidate();
    }

    public void showBorder(boolean show) {
        mShowBorder = show;
        invalidate();
    }

    public void setBorderWidth(int width) {
        mBorderWidth = width;
        mBorderHalf = Math.round(mBorderWidth / 2);
        if (mBorderHalf == 0) {
            mBorderHalf = 1;
        }

        mPaint.setStrokeWidth(mBorderWidth);
        updateCircleBorder();
        updateRectangleBorder();
        invalidate();
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    // helper reusable vars, just IGNORE
    private float halfWidth, halfHeight, centerX, centerY;
    private RectF rect = new RectF(0, 0, 0, 0);
    private RectF rectBorder = new RectF(0, 0, 0, 0);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // just calculate both shapes, is not heavy

        // rounded corners path
        rect.left = 0;
        rect.top = 0;
        rect.right = w;
        rect.bottom = h;
        mPathCorners.reset();
        mPathCorners.addRoundRect(rect, mCornerRadius, mCornerRadius, Path.Direction.CW);
        mPathCorners.close();

        // circle path
        halfWidth = w / 2f;
        halfHeight = h / 2f;
        centerX = halfWidth;
        centerY = halfHeight;
        mPathCircle.reset();
        mPathCircle.addCircle(centerX, centerY, Math.min(halfWidth, halfHeight), Path.Direction.CW);
        mPathCircle.close();

        updateRectangleBorder();
        updateCircleBorder();
    }

    // helper reusable var, just IGNORE
    private int save;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        save = canvas.save();
        canvas.clipPath(mIsCircleShape ? mPathCircle : mPathCorners);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);

        if (mShowBorder) {
            canvas.drawPath(mIsCircleShape ? mPathCircleBorder : mPathCornersBorder, mPaint);
        }
    }

    private void updateCircleBorder() {
        // border path for circle
        mPathCircleBorder.reset();
        mPathCircleBorder.addCircle(centerX, centerY, Math.min(halfWidth - mBorderHalf,
                halfHeight - mBorderHalf), Path.Direction.CW);
        mPathCircleBorder.close();
    }

    private void updateRectangleBorder() {
        // border path for rectangle
        rectBorder.left = rect.left + mBorderHalf;
        rectBorder.top = rect.top + mBorderHalf;
        rectBorder.right = rect.right - mBorderHalf;
        rectBorder.bottom = rect.bottom - mBorderHalf;
        mPathCornersBorder.reset();
        mPathCornersBorder.addRoundRect(rectBorder, mCornerRadius - mBorderHalf, mCornerRadius -
                mBorderHalf, Path.Direction.CW);
        mPathCornersBorder.close();
    }
}

