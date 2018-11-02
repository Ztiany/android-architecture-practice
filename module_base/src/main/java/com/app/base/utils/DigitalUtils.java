package com.app.base.utils;

import java.text.NumberFormat;

public class DigitalUtils {

    private static NumberFormat mNumberFormat = NumberFormat.getNumberInstance();

    @SuppressWarnings("all")
    public static String formatDecimal(float number, int minimumFraction, int maximumFraction) {
        mNumberFormat.setMinimumFractionDigits(minimumFraction);
        mNumberFormat.setMaximumFractionDigits(maximumFraction);
        return mNumberFormat.format(number);
    }

    /**
     * @param number 格式化数字
     * @param keep   保留的分数
     * @return String
     */
    @SuppressWarnings("all")
    public static String formatDecimal(float number, int keep) {
        return formatDecimal(number, keep, keep);
    }

    @SuppressWarnings("unused")
    public static String formatDecimalDefault(float number) {
        return formatDecimal(number, 0);
    }


}
