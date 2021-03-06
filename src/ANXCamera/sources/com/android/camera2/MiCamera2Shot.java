package com.android.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.os.Handler;
import com.android.camera.Thumbnail;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy;
import com.xiaomi.camera.core.ParallelCallback;
import com.xiaomi.camera.core.ParallelTaskData;

public abstract class MiCamera2Shot<T> {
    protected static final long DEFAULT_TASK_DATA_TIMESTAMP = 0;
    private static final int SHUTTER_FRAMENUM_CAPTUREING = 1;
    private static final int SHUTTER_FRAMENUM_NONE = 0;
    private static final int SHUTTER_FRAMENUM_SHUTTERED = 2;
    protected Handler mCameraHandler;
    protected boolean mDeparted;
    protected MiCamera2 mMiCamera;
    private ParallelCallback mParallelCallback;
    private Camera2Proxy.PictureCallback mPictureCallback;
    protected int mPreviewThumbnailHash = -1;
    private boolean mQuickShotAnimation;
    private int mShutterFrameNum;

    public MiCamera2Shot(MiCamera2 miCamera2) {
        this.mMiCamera = miCamera2;
        this.mCameraHandler = miCamera2.getCameraHandler();
        this.mShutterFrameNum = 0;
    }

    /* access modifiers changed from: protected */
    public abstract CameraCaptureSession.CaptureCallback generateCaptureCallback();

    /* access modifiers changed from: protected */
    public final ParallelTaskData generateParallelTaskData(long j) {
        Camera2Proxy.PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback == null) {
            Log.e(getClass().getSimpleName(), "null callback is not allowed!");
            return null;
        }
        ParallelTaskData parallelTaskData = new ParallelTaskData(this.mMiCamera.getId(), j, this.mMiCamera.getCameraConfigs().getShotType(), this.mMiCamera.getCameraConfigs().getShotPath(), this.mMiCamera.getCameraConfigs().getCaptureTime());
        return pictureCallback.onCaptureStart(parallelTaskData, this.mMiCamera.getPictureSize(), isQuickShotAnimation());
    }

    /* access modifiers changed from: protected */
    public abstract CaptureRequest.Builder generateRequestBuilder() throws CameraAccessException, IllegalStateException;

    public ParallelCallback getParallelCallback() {
        return this.mParallelCallback;
    }

    public Camera2Proxy.PictureCallback getPictureCallback() {
        return this.mPictureCallback;
    }

    /* access modifiers changed from: protected */
    public boolean isInQcfaMode() {
        return this.mMiCamera.getCapabilities().isSupportedQcfa() && (this.mMiCamera.getCapabilities().getOperatingMode() == 32775 || this.mMiCamera.getCapabilities().getOperatingMode() == 33013 || this.mMiCamera.getCapabilities().getOperatingMode() == 33011);
    }

    public boolean isQuickShotAnimation() {
        return this.mQuickShotAnimation;
    }

    /* access modifiers changed from: protected */
    public void makeClobber() {
        this.mDeparted = true;
    }

    /* access modifiers changed from: protected */
    public abstract void notifyResultData(T t);

    /* access modifiers changed from: protected */
    public abstract void onImageReceived(Image image, int i);

    public boolean onPreviewComing() {
        if (!isQuickShotAnimation()) {
            return false;
        }
        int i = this.mShutterFrameNum;
        if (i == 0) {
            return false;
        }
        if (i >= 2) {
            return true;
        }
        this.mShutterFrameNum = i + 1;
        if (this.mShutterFrameNum != 2) {
            return false;
        }
        this.mShutterFrameNum = 2;
        Camera2Proxy.PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onCaptureShutter(true);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final void onPreviewThumbnailReceived(Thumbnail thumbnail) {
        this.mPreviewThumbnailHash = thumbnail.hashCode();
    }

    /* access modifiers changed from: protected */
    public abstract void prepare();

    public void setParallelCallback(ParallelCallback parallelCallback) {
        this.mParallelCallback = parallelCallback;
    }

    public void setPictureCallback(Camera2Proxy.PictureCallback pictureCallback) {
        this.mPictureCallback = pictureCallback;
    }

    public void setQuickShotAnimation(boolean z) {
        this.mQuickShotAnimation = z;
    }

    /* access modifiers changed from: protected */
    public abstract void startSessionCapture();

    /* access modifiers changed from: protected */
    public final void startShot() {
        prepare();
        startSessionCapture();
        this.mShutterFrameNum = 1;
    }
}
