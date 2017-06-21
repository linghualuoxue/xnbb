package com.bj.xnbb.common;

import java.text.DecimalFormat;

/**常量类
 * Created by XNBB on 2017/6/15.
 */
public class Constant {

    /**
     * 字段分隔符
     */
    public static final String  SEPARATOR = "\001";

    /**
     * reduce结果分隔符
     */
    public static final String  SEPARATOR_TAB = "\t";

    /**
     * 文件名称分隔符
     */
    public static final String FILE_NAME_SPLIT = "_";

    /**
     * 场强常量
     */
    public static final float FIELD_STRENGTH_CONSTANT =  107.0f;

    /**
     * 门限5
     */
    public static final float  THRESHOLD_5 = 5f;
    /**
     * 门限10
     */
    public static final float  THRESHOLD_10 = 10f;
    /**
     * 门限20
     */
    public static final float  THRESHOLD_20 = 20f;
    /**
     * 门限30
     */
    public static final float  THRESHOLD_30 = 30f;

    /**
     * 占用
     */
    public static int OCCUPY = 1;

    /**
     * 非占用
     */
   public static int UNOCCUPY = 0;

    /**
     * 划分的份数
     */
   public static int COPIES =10;

    /**
     * 保留两位有效数字
     */
    public static DecimalFormat format2 = new DecimalFormat("0.##");
    /**
     * 保留三位有效数字
     */
    public static DecimalFormat format3 = new DecimalFormat("0.###");
}
