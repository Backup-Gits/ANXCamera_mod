package com.android.camera.ui.drawable.snap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.view.ViewCompat;
import com.android.camera.Util;
import com.android.camera.constant.ColorConstant;
import com.android.camera.ui.drawable.CameraPaintBase;
import java.util.ArrayList;
import java.util.List;

public class CameraSnapPaintCircle extends CameraPaintBase {
    public boolean isInBeautyMode;
    private RectF mArcRectF;
    private int mBeautyColor = ColorConstant.COLOR_COMMON_SELECTED_BEAUTY;
    private boolean mNeedProcessShader;
    private boolean mNeedRing = false;
    private boolean mNoNeedArc;
    private Paint mOffsetPaint;
    private int mProcessColor;
    private Shader mProcessShader;
    private float mRingWidth;
    private List<Float> mSegmentRatios;
    private List<Long> mSegmentTimes;
    private float mSpaceAngle = 3.0f;

    public CameraSnapPaintCircle(Context context) {
        super(context);
    }

    private void drawElapsedArc(Canvas canvas, float f2, float f3) {
        RectF rectF = this.mArcRectF;
        float f4 = this.mSpaceAngle;
        float f5 = -90.0f + f2 + f4;
        float f6 = (f3 - f2) - f4;
        canvas.drawArc(rectF, f5, f6, false, this.mPaint);
    }

    private void drawOffset(Canvas canvas, float f2) {
        RectF rectF = this.mArcRectF;
        float f3 = this.mSpaceAngle;
        canvas.drawArc(rectF, (f2 - 0.049804688f) - f3, f3 * 2.0f, false, this.mOffsetPaint);
    }

    public void addSegmentNow(long j) {
        if (this.mSegmentRatios == null) {
            this.mSegmentRatios = new ArrayList();
        }
        this.mSegmentRatios.add(Float.valueOf(this.timeAngle));
        if (this.mSegmentTimes == null) {
            this.mSegmentTimes = new ArrayList();
        }
        this.mSegmentTimes.add(Long.valueOf(j));
    }

    public void clearSegments() {
        List<Float> list = this.mSegmentRatios;
        if (list != null) {
            list.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        if (this.mNoNeedArc) {
            this.isRecording = false;
        }
        if (this.isRecording) {
            float f2 = this.mBaseRadius * this.mCurrentWidthPercent;
            float f3 = this.mMiddleX;
            float f4 = f3 - f2;
            float f5 = this.mMiddleY;
            float f6 = f5 - f2;
            float f7 = f3 + f2;
            float f8 = f5 + f2;
            if (this.mNeedProcessShader) {
                this.mPaint.setShader((Shader) null);
            }
            this.mPaint.setAlpha(this.isInBeautyMode ? this.mCurrentAlpha : CameraPaintBase.ALPHA_HINT);
            this.mArcRectF.set(f4, f6, f7, f8);
            canvas.drawArc(this.mArcRectF, -90.0f, 360.0f, false, this.mPaint);
            this.mPaint.setAlpha(CameraPaintBase.ALPHA_OPAQUE);
            if (this.mNeedProcessShader) {
                this.mPaint.setColor(this.mProcessColor);
            }
            if (this.isInBeautyMode) {
                this.mPaint.setColor(this.mBeautyColor);
            }
            canvas.drawArc(this.mArcRectF, (this.isClockwise ? 0.0f : this.timeAngle) - 0.049804688f, Math.max(0.0f, this.isClockwise ? this.timeAngle : 360.0f - this.timeAngle), false, this.mPaint);
            this.mPaint.setColor(this.mCurrentColor);
            this.mOffsetPaint.setStrokeWidth(this.mPaint.getStrokeWidth() + 1.0f);
            if (this.timeAngle != 0.0f) {
                drawOffset(canvas, 0.0f);
                drawOffset(canvas, this.timeAngle);
            }
            List<Float> list = this.mSegmentRatios;
            if (list != null) {
                for (Float floatValue : list) {
                    drawOffset(canvas, floatValue.floatValue());
                }
            }
        } else if (this.mNeedRing) {
            this.mPaint.setAlpha(CameraPaintBase.ALPHA_OUTSTANDING);
            canvas.drawCircle(this.mMiddleX, this.mMiddleY, this.mBaseRadius * this.mRingWidth, this.mPaint);
            this.mPaint.setAlpha(this.mCurrentAlpha);
        } else {
            canvas.drawCircle(this.mMiddleX, this.mMiddleY, this.mBaseRadius * this.mCurrentWidthPercent, this.mPaint);
        }
    }

    public List<Float> getSegmentRatios() {
        return this.mSegmentRatios;
    }

    public boolean hasSegments() {
        List<Float> list = this.mSegmentRatios;
        return list != null && !list.isEmpty();
    }

    /* access modifiers changed from: protected */
    public void initPaint(Context context) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth((float) Util.dpToPixel(1.0f));
        this.mArcRectF = new RectF();
        this.mOffsetPaint = new Paint();
        this.mOffsetPaint.setAntiAlias(true);
        this.mOffsetPaint.setStyle(Paint.Style.STROKE);
        this.mOffsetPaint.setStrokeWidth((float) Util.dpToPixel(1.0f));
        this.mOffsetPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mOffsetPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void initVVProcessShader() {
    }

    public long removeLastSegmentAndGetLastTime() {
        if (this.mSegmentRatios.isEmpty()) {
            return 0;
        }
        List<Float> list = this.mSegmentRatios;
        list.remove(list.size() - 1);
        List<Long> list2 = this.mSegmentTimes;
        list2.remove(list2.size() - 1);
        if (this.mSegmentRatios.isEmpty()) {
            this.timeAngle = 0.0f;
            return 0;
        }
        List<Float> list3 = this.mSegmentRatios;
        this.timeAngle = list3.get(list3.size() - 1).floatValue();
        if (this.mSegmentTimes.isEmpty()) {
            return 0;
        }
        List<Long> list4 = this.mSegmentTimes;
        return list4.get(list4.size() - 1).longValue();
    }

    public void resetRecordingState() {
        super.resetRecordingState();
        setNeedSplit(false);
        setNeedProcessShade(false);
    }

    public void setNeedProcessShade(boolean z) {
        this.mNeedProcessShader = z;
        if (z) {
            this.mProcessColor = -1417668;
        } else {
            this.mPaint.setShader((Shader) null);
        }
    }

    public void setNeedSplit(boolean z) {
        this.mNoNeedArc = !z;
        setSpaceAngel(z ? 1.5f : 0.0f);
    }

    public void setRingVisible(int i) {
        this.mNeedRing = i == 0;
    }

    public void setSpaceAngel(float f2) {
        this.mSpaceAngle = f2;
    }

    public void updateValue(float f2) {
        super.updateValue(f2);
        float f3 = this.mCurrentWidthPercent;
        this.mRingWidth = f3 + (0.4f * f3 * f2);
    }
}
