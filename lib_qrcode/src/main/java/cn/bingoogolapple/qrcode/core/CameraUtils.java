package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import io.fotoapparat.parameter.Size;

final class CameraUtils {

    private static final double MAX_ASPECT_DISTORTION = 0.15;//最大比例偏差
    private static final int MIN_PREVIEW_PIXELS = 480 * 800;//小于此预览尺寸直接移除

    private static final class SizeComparator implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return -(rhs.width * rhs.height - lhs.width * lhs.height);
        }
    }

    static Size findBestPictureSize(Context context, Collection<Size> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Size bestPictureResolution = findBestPictureResolution(new int[]{displayMetrics.heightPixels, displayMetrics.widthPixels}, new ArrayList<>(collection));
        Log.d("CameraUtils", "bestPictureResolution:" + bestPictureResolution);
        return bestPictureResolution;
    }

    static Size findBestPreviewSize(Context context, Collection<Size> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Size bestPreviewResolution = findBestPreviewResolution(new int[]{displayMetrics.heightPixels, displayMetrics.widthPixels}, new ArrayList<>(collection));
        Log.d("CameraUtils", "bestPreviewResolution:" + bestPreviewResolution);
        return bestPreviewResolution;
    }

    /**
     * 找到最好的适配分辨率
     */
    private static Size findBestPictureResolution(int[] screenSize, List<Size> rawSupportedPicResolutions) {
        // 排序
        List<Size> sortedSupportedPicResolutions = new ArrayList<>(rawSupportedPicResolutions);
        //递增排序，重要
        Collections.sort(sortedSupportedPicResolutions, new SizeComparator());
        // 移除不符合条件的分辨率——高：宽
        float screenAspectRatio = 1.0F * screenSize[0] / screenSize[1];
        Log.d("CameraUtils", "screenSize:" + Arrays.toString(screenSize));
        Log.d("CameraUtils", "screenAspectRatio:" + screenAspectRatio);
        Iterator<Size> it = sortedSupportedPicResolutions.iterator();

        while (it.hasNext()) {
            Size size = it.next();
            int width = size.width;
            int height = size.height;
            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            float aspectRatio = 1.0F * maybeFlippedHeight / maybeFlippedWidth;
            Log.d("CameraUtils", "maybeFlippedWidth:" + maybeFlippedWidth + " maybeFlippedHeight:" + maybeFlippedHeight);
            Log.d("CameraUtils", "aspectRatio:" + aspectRatio);
            float distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {//移除不满足比例的分辨率
                it.remove();
            }
        }
        if (sortedSupportedPicResolutions.isEmpty()) {
            return rawSupportedPicResolutions.get(rawSupportedPicResolutions.size() - 1);
        }
        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的
        return sortedSupportedPicResolutions.get(sortedSupportedPicResolutions.size() - 1);

    }


    private static Size findBestPreviewResolution(int[] screenSize, List<Size> rawSupportedSizes) {
        // 按照分辨率从大到小排序
        List<Size> supportedPreviewResolutions = new ArrayList<>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new SizeComparator());
        // 移除不符合条件的分辨率——高：宽
        double screenAspectRatio = 1.0F * screenSize[0] / screenSize[1];

        Iterator<Size> it = supportedPreviewResolutions.iterator();
        Size size;
        while (it.hasNext()) {
            size = it.next();
            int width = size.width;
            int height = size.height;
            // 移除低于下限的分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }
            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            float aspectRatio = 1.0F * maybeFlippedHeight / maybeFlippedWidth;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {//移除不符合比例的分辨率
                it.remove();
                continue;
            }
            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == screenSize[0] && maybeFlippedHeight == screenSize[1]) {
                return size;
            }
        }
        if (supportedPreviewResolutions.isEmpty()) {
            return rawSupportedSizes.get(rawSupportedSizes.size() - 1);
        }
        // 如果没有找到最合适的
        return supportedPreviewResolutions.get(supportedPreviewResolutions.size() - 1);
    }


}
