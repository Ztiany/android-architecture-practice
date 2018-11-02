package com.app.base.data;

/**
 * 用于定义一些业务上的常量,以及通用的标识判断
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:08
 */
public class Businesser {

    ///////////////////////////////////////////////////////////////////////////
    // 后台返回统一的true和false状态
    ///////////////////////////////////////////////////////////////////////////

    public static final int FLAG_POSITIVE = 1;
    public static final int FLAG_NEGATIVE = 0;

    public static boolean isFlagPositive(int flag) {
        return flag == FLAG_POSITIVE;
    }

    public static boolean isDefault(int flagCode) {
        return FLAG_POSITIVE == flagCode;
    }

    public static boolean isSelected(int flag) {
        return FLAG_POSITIVE == flag;
    }


}
