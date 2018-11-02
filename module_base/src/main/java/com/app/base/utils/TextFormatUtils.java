package com.app.base.utils;


import android.text.TextUtils;

import com.android.base.utils.android.ResourceUtils;
import com.app.base.R;


/**
 * 仅适用于接口或者界面中的一些文本格式化
 */
public class TextFormatUtils {

    public static final String COLON = ":";
    public static final String WHIPPLETREE = "-";
    public static final String DOUBLE_WHIPPLETREE = "-";
    public static final String BLANK = " ";
    public static final String COMMA = "、";

    private TextFormatUtils() {
        throw new UnsupportedOperationException();
    }

    public static String jointIntArr(String jointMark, int... arr) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.valueOf(arr[i]));
            if (i != length - 1) {
                stringBuilder.append(jointMark);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 使用符号拼接元素
     *
     * @return 拼接之后的元素
     */
    public static String jointText(String jointMark, Object... textArr) {
        if (textArr.length == 1) {
            return String.valueOf(textArr[0]);
        }
        String result = "";
        String realText;
        for (int i = 0; i < textArr.length; i++) {
            realText = textArr[i] == null ? "" : String.valueOf(textArr[i]);
            if (!TextUtils.isEmpty(realText)) {
                if (i == 0) {
                    result += realText;
                } else {
                    boolean empty = (textArr[i]) == null || TextUtils.isEmpty(String.valueOf(textArr[i]));
                    if (!empty) {
                        if (TextUtils.isEmpty(result) || result.endsWith(jointMark)) {
                            result += realText;
                        } else {
                            result += jointMark.concat(realText);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 浮点数值 格式化成金额表示,比如：1365.00 -->  ¥ 1,365
     *
     * @param floatValue 浮点金额
     * @return 格式化后的文本
     */
    public static String formatFloatToCurrency(float floatValue) {
        int length = getLength(floatValue);
        return addCurrencySymbol(DigitalUtils.formatDecimal(floatValue, length));
    }

    /**
     * 浮点数值 格式化成金额表示,比如：1365.00 -->  1,365
     *
     * @param floatValue 浮点金额
     * @return 格式化后的文本
     */
    public static String formatFloat(float floatValue) {
        int length = getLength(floatValue);
        return DigitalUtils.formatDecimal(floatValue, length);
    }

    private static int getLength(float floatValue) {
        int length;
        String value = String.valueOf(floatValue);
        int i = value.lastIndexOf(".");
        if (i == -1) {
            length = 0;
        } else {
            float decimals = Float.parseFloat(value.substring(i + 1));
            if (decimals == 0) {
                length = 0;
            } else {
                length = value.length() - i - 1;
                length = length < 0 ? 0 : length > 2 ? 2 : length;
            }
        }
        return length;
    }

    public static String addCurrencySymbol(String content) {
        return ResourceUtils.getString(R.string.currency_mask, content);
    }

    public static String addBrackets(String content) {
        return TextUtils.isEmpty(content) ? "" : ResourceUtils.getString(R.string.brackets_mask, content);
    }


}
