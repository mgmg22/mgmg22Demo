package com.google.mgmg22.lib_slide_back.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.google.mgmg22.lib_slide_back.SmartSwipe;

import static com.google.mgmg22.lib_slide_back.SwipeConsumer.DIRECTION_ALL;
import static com.google.mgmg22.lib_slide_back.SwipeConsumer.DIRECTION_LEFT;


/**
 * @author billy.qi
 */
public class ScrimView extends View {
    public static float MAX_PROGRESS = 1F;
    public static float MIN_PROGRESS = 0F;
    private final Paint mPaint;
    private int mSize = 60;
    private Rect mBounds = new Rect();
    private int mScrimColor;
    private int mBaseAlpha;
    private int mDirection;
    private Paint mShadowPaint;
    private Rect mShadowRect = new Rect();
    private int mShadowColor = 0x80000000;
    private int mShadowDirection;

    public ScrimView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mShadowPaint = new Paint();
        mShadowPaint.setDither(true);
        mShadowPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mBounds.right = w;
        this.mBounds.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mScrimColor != 0) {
            canvas.drawRect(mBounds, mPaint);
        }
        if (mSize > 0 && mShadowColor != 0 && (mDirection & DIRECTION_ALL) > 0) {
            canvas.save();
            canvas.clipRect(mShadowRect);
            canvas.drawPaint(mShadowPaint);
            canvas.restore();
        }
    }

    public void setProgress(float progress) {
        float mProgress = SmartSwipe.ensureBetween(progress, MIN_PROGRESS, MAX_PROGRESS);
        final int alpha = (int) (mBaseAlpha * mProgress);
        final int color = alpha << 24 | (mScrimColor & 0xFFFFFF);
        mPaint.setColor(color);
    }

    public void setScrimColor(int scrimColor) {
        this.mScrimColor = scrimColor;
        mBaseAlpha = (mScrimColor & 0xFF000000) >>> 24;
    }

    public void setDirection(int direction, int shadowColor, int shadowDirection, int shadowSize, int parentWidth, int parentHeight) {
        this.mDirection = direction;
        this.mShadowColor = shadowColor;
        this.mShadowDirection = shadowDirection;
        this.mSize = shadowSize;
        if (mShadowColor == 0) {
            return;
        }
        int l = 0, t = 0, r, b;
        switch (mShadowDirection) {
            default:
                return;
            case DIRECTION_LEFT:
                r = mSize;
                t = b = parentHeight;
                break;
        }
        mShadowRect.right = r;
        mShadowRect.bottom = b;
        int alpha = (mShadowColor & 0xFF000000) >>> 24;
        int steps = 30;
        float[] positions = new float[steps + 1];
        int[] colors = new int[steps + 1];
        boolean revert = mShadowDirection == DIRECTION_LEFT;
        for (int i = 0; i <= steps; i++) {
            positions[i] = i * 1F / steps;
        }
        float position;
        for (int i = 0; i <= steps; i++) {
            position = positions[revert ? steps - i : i];
            colors[i] = ((int) (alpha * position * position) << 24) | (mShadowColor & 0xFFFFFF);
        }

        boolean horizontal = direction == DIRECTION_LEFT;
        if (horizontal) {
            t = b = b >> 1;
        } else {
            l = r = r >> 1;
        }

        LinearGradient shader = new LinearGradient(l, t, r, b, colors, positions, LinearGradient.TileMode.CLAMP);
        mShadowPaint.setShader(shader);
    }
}
