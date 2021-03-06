package com.android.camera.fragment.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.beauty.SingleCheckAdapter;
import com.android.camera.ui.SeekBarCompat;
import java.util.List;

public class BeautySmoothLevelFragment extends BaseBeautyFragment {
    private static final int INTERVAL = 5;
    /* access modifiers changed from: private */
    public SeekBarCompat mAdjustSeekBar;
    /* access modifiers changed from: private */
    public boolean mIsRTL;
    /* access modifiers changed from: private */
    public int mSeekBarMaxProgress = 100;

    private void initView(View view) {
        this.mIsRTL = Util.isLayoutRTL(getContext());
        this.mAdjustSeekBar = (SeekBarCompat) view.findViewById(R.id.beauty_level_adjust_seekbar);
        initBeautyItems();
        this.mSeekBarMaxProgress = 100;
        int faceBeautyRatio = CameraSettings.getFaceBeautyRatio("pref_beautify_skin_smooth_ratio_key");
        int defaultValueByKey = BeautyConstant.getDefaultValueByKey("pref_beautify_skin_smooth_ratio_key");
        this.mAdjustSeekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_style));
        this.mAdjustSeekBar.setMax(this.mSeekBarMaxProgress);
        this.mAdjustSeekBar.setSeekBarPinProgress(defaultValueByKey);
        this.mAdjustSeekBar.setProgress(faceBeautyRatio);
        this.mAdjustSeekBar.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarCompatChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                BeautySmoothLevelFragment.this.onLevelSelected(i);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.mAdjustSeekBar.setOnSeekBarCompatTouchListener(new SeekBarCompat.OnSeekBarCompatTouchListener() {
            private int getNextProgress(MotionEvent motionEvent) {
                int width = BeautySmoothLevelFragment.this.mAdjustSeekBar.getWidth();
                int x = (int) (((BeautySmoothLevelFragment.this.mIsRTL ? ((float) width) - motionEvent.getX() : motionEvent.getX()) / ((float) width)) * ((float) BeautySmoothLevelFragment.this.mSeekBarMaxProgress));
                int pinProgress = BeautySmoothLevelFragment.this.mAdjustSeekBar.getPinProgress();
                if (pinProgress > 0 && (motionEvent.getAction() == 2 || motionEvent.getAction() == 1)) {
                    if (Math.abs(x + 0) <= 5) {
                        x = 0;
                    } else if (Math.abs(x - pinProgress) <= 5) {
                        x = pinProgress;
                    } else if (Math.abs(x - BeautySmoothLevelFragment.this.mSeekBarMaxProgress) <= 5) {
                        x = BeautySmoothLevelFragment.this.mSeekBarMaxProgress;
                    }
                }
                return BeautySmoothLevelFragment.this.mAdjustSeekBar.isCenterTwoWayMode() ? Util.clamp(x - pinProgress, 0 - pinProgress, BeautySmoothLevelFragment.this.mSeekBarMaxProgress - pinProgress) : Util.clamp(x, 0, BeautySmoothLevelFragment.this.mSeekBarMaxProgress);
            }

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    if (!BeautySmoothLevelFragment.this.mAdjustSeekBar.getThumb().getBounds().contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        return true;
                    }
                } else if (!(action == 1 || action == 2)) {
                    return false;
                }
                BeautySmoothLevelFragment.this.mAdjustSeekBar.setProgress(getNextProgress(motionEvent));
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void onLevelSelected(int i) {
        DataRepository.dataItemGlobal().getCurrentMode();
        CameraSettings.setFaceBeautyRatio("pref_beautify_skin_smooth_ratio_key", i);
        ShineHelper.onBeautyChanged();
    }

    /* access modifiers changed from: protected */
    public int beautyLevelToPosition(int i, int i2) {
        return Util.clamp(i, 0, i2);
    }

    /* access modifiers changed from: protected */
    public View getAnimateView() {
        return this.mAdjustSeekBar;
    }

    /* access modifiers changed from: protected */
    public List<SingleCheckAdapter.LevelItem> initBeautyItems() {
        return null;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_beauty_smooth, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndJumpOut() {
        super.onViewCreatedAndJumpOut();
        this.mAdjustSeekBar.setEnabled(false);
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndVisibleToUser(boolean z) {
        super.onViewCreatedAndVisibleToUser(z);
        this.mAdjustSeekBar.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public int provideItemHorizontalMargin() {
        return getResources().getDimensionPixelSize(R.dimen.beautycamera_beauty_level_item_margin);
    }

    public void setEnableClick(boolean z) {
        this.mAdjustSeekBar.setEnabled(z);
    }
}
