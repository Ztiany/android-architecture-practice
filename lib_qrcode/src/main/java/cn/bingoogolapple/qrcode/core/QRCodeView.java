package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatBuilder;
import io.fotoapparat.FotoapparatSwitcher;
import io.fotoapparat.hardware.provider.CameraProvider;
import io.fotoapparat.hardware.provider.CameraProviders;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.parameter.selector.FlashSelectors;
import io.fotoapparat.parameter.selector.Selectors;
import io.fotoapparat.parameter.update.UpdateRequest;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.parameter.selector.FlashSelectors.off;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;

public abstract class QRCodeView extends FrameLayout implements ProcessDataTask.Delegate {

    private static final String TAG = QRCodeView.class.getSimpleName();

    private CameraView mCameraView;
    private FotoapparatSwitcher fotoapparatSwitcher;

    protected ScanBoxView mScanBoxView;
    protected Delegate mDelegate;

    protected ProcessDataTask mProcessDataTask;
    protected boolean mSpotAble = false;

    private Rect framingRect = new Rect();
    private Rect framingRectInPreview;

    public QRCodeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mCameraView = new CameraView(getContext());
        mScanBoxView = new ScanBoxView(getContext());
        mScanBoxView.initCustomAttrs(context, attrs);
        addView(mCameraView);
        addView(mScanBoxView);
        fotoapparatSwitcher = FotoapparatSwitcher.withDefault(createFotoapparat());
    }

    private Fotoapparat createFotoapparat() {

        CameraProvider cameraProvider;

        if (Build.VERSION.SDK_INT >= 21) {
            cameraProvider = CameraProviders.v1();
        } else {
            cameraProvider = CameraProviders.v2(getContext());
        }

        FotoapparatBuilder builder = Fotoapparat
                .with(getContext())
                .cameraProvider(cameraProvider)
                .into(mCameraView)
                .lensPosition(lensPosition(LensPosition.BACK))
                .frameProcessor(this::processFrame)
                .focusMode(Selectors.firstAvailable(continuousFocus(), autoFocus(), fixed()))
                .flash(off())
                .logger(s -> Log.i(TAG, s));

        return builder.build();
    }

    private void processFrame(Frame frame) {
        ProcessDataTask processDataTask = mProcessDataTask;
        if (mSpotAble && (processDataTask == null || processDataTask.isCancelled())) {
            mProcessDataTask = new ProcessDataTask(frame.image, frame.size, frame.rotation, this) {
                @Override
                protected void onPostExecute(String result) {
                    if (mSpotAble) {
                        if (mDelegate != null && !TextUtils.isEmpty(result)) {
                            try {
                                mDelegate.onScanQRCodeSuccess(result);
                                stopSpot();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    cancelProcessDataTask();
                }
            }.perform();
        }
    }

    /**
     * 设置扫描二维码的代理
     *
     * @param delegate 扫描二维码的代理
     */
    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    /**
     * 显示扫描框
     */
    public void showScanRect() {
        if (mScanBoxView != null) {
            mScanBoxView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏扫描框
     */
    public void hiddenScanRect() {
        if (mScanBoxView != null) {
            mScanBoxView.setVisibility(View.GONE);
        }
    }

    /**
     * 打开后置摄像头开始预览，但是并未开始识别
     */
    public void startCamera() {
        try {
            fotoapparatSwitcher.start();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 关闭摄像头预览，并且隐藏扫描框
     */
    public void stopCamera() {
        stopSpotAndHiddenRect();
        try {
            fotoapparatSwitcher.stop();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 延100开始识别
     */
    public void startSpot() {
        postDelayed(() -> {
            mSpotAble = true;
            startCamera();
        }, 100);
    }

    /**
     * 停止识别
     */
    public void stopSpot() {
        cancelProcessDataTask();
        mSpotAble = false;
    }

    /**
     * 停止识别，并且隐藏扫描框
     */
    public void stopSpotAndHiddenRect() {
        stopSpot();
        hiddenScanRect();
    }

    /**
     * 显示扫描框，并且延迟1.5秒后开始识别
     */
    public void startSpotAndShowRect() {
        startSpot();
        showScanRect();
    }

    /**
     * 打开闪光灯
     */
    public void openFlashlight() {
        try {
            fotoapparatSwitcher.getCurrentFotoapparat().updateParameters(UpdateRequest.builder().flash(FlashSelectors.on()).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭散光灯
     */
    public void closeFlashlight() {
        try {
            fotoapparatSwitcher.getCurrentFotoapparat().updateParameters(UpdateRequest.builder().flash(FlashSelectors.off()).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁二维码扫描控件
     */
    public void onDestroy() {
        mDelegate = null;
    }

    /**
     * 取消数据处理任务
     */
    protected void cancelProcessDataTask() {
        if (mProcessDataTask != null) {
            mProcessDataTask.cancelTask();
            mProcessDataTask = null;
        }
    }

    /**
     * 切换成扫描条码样式
     */
    public void changeToScanBarcodeStyle() {
        if (!mScanBoxView.getIsBarcode()) {
            mScanBoxView.setIsBarcode(true);
        }
    }

    /**
     * 切换成扫描二维码样式
     */
    public void changeToScanQRCodeStyle() {
        if (mScanBoxView.getIsBarcode()) {
            mScanBoxView.setIsBarcode(false);
        }
    }

    /**
     * 当前是否为条码扫描样式
     *
     * @return
     */
    public boolean isScanBarcodeStyle() {
        return mScanBoxView.getIsBarcode();
    }

    protected Rect getFramingRectInPreview(int previewWidth, int previewHeight) {
        if (!mScanBoxView.getScanBoxAreaRect(framingRect)) {
            return null;
        }
        if (framingRectInPreview == null) {
            Rect rect = new Rect(framingRect);
            Point cameraResolution = new Point(previewWidth, previewHeight);
            Point screenResolution = Utils.getScreenResolution(getContext());
            float x = (cameraResolution.x * 1.0F / screenResolution.x);
            float y = (cameraResolution.y * 1.0F / screenResolution.y);
            rect.left = (int) (rect.left * x);
            rect.right = (int) (rect.right * x);
            rect.top = (int) (rect.top * y);
            rect.bottom = (int) (rect.bottom * y);
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

    public interface Delegate {

        /**
         * 处理扫描结果
         */
        void onScanQRCodeSuccess(String result);

        /**
         * 处理打开相机出错
         */
        void onScanQRCodeOpenCameraError();

    }
}