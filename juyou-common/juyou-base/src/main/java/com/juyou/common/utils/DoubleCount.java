package com.juyou.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @功能 处理double计算精度问题
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
**/
public class DoubleCount {

    /**
     * 相减
     * @param d1
     * @param d2
     * @return
     */
    public static Double sub(Double d1, Double d2) {

        if (d1 == null || d2 == null) {
            return null;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).doubleValue();

    }

    /**
     * 两个Double数相加
     * @param d1
     * @param d2
     * @return
     */
    public static Double add(Double d1, Double d2) {
        if (d1 == null || d2 == null) {
            return null;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();

    }


    /**
     * 两个Double数相除，并保留scale位小数
     * @param d1
     * @param d2
     * @param scale
     * @return
     */
    public static Double div(Double d1, Double d2, int scale) {
        if (d1 == null || d2 == null || scale < 0) {
            return null;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();

    }

    /**
     * 两个Double数相乘
     * @param d1
     * @param d2
     * @return
     */
    public static Double mul(Double d1, Double d2) {

        if (d1 == null || d2 == null) {
            return null;
        }
        BigDecimal b1 = new BigDecimal(d1.toString());

        BigDecimal b2 = new BigDecimal(d2.toString());

        return b1.multiply(b2).doubleValue();

    }

    /**

     * 遇到.5的情况时往上近似

     *

     * @param d

     * @param scale

     * @return

     */

    public static Double setDoubleScale(Double d, int scale) {
        if (d == null || scale < 0) {
            return null;
        }
        BigDecimal b = new BigDecimal(d);

        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

}
