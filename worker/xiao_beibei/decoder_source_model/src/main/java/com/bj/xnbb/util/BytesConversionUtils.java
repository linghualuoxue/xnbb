package com.bj.xnbb.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * byte[] 数组转化为其他类型的工具类
 * Created by XNBB on 2017/6/14.
 */
public class BytesConversionUtils {

    /**
     * byte数组转为两位的int
     * @param res
     * @return
     */
    public static int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
        // | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    /**
     * byte数组转为四位的数组
     * @param res
     * @return
     */
    public static int byte2int4(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }


    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }
    public static short getShortOne(byte[] b, int index) {
        return (short) ((b[index + 0] & 0xff));
    }

    /**
     * byte转化为8位的long类型
     * @param b
     * @return
     */
    public static long bytes2long(byte[] b) {
        long temp = 0;
        long res = 0;
        for (int i=0;i<8;i++) {
            res <<= 8;
            temp = b[i] & 0xff;
            res |= temp;
        }
        return res;
    }


    /**
     * byte转化为char类型
     * @param bytes
     * @return
     */
    private static String getChars (byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);

        return new String(cb.array());
    }


    /**
     * byte[] 转化成double类型
     * @param b
     * @param index
     * @return
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    /**
     * 转化为8位的long类型
     * @param b
     * @param index
     * @return
     */
    public static long getLong8(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return l;
    }

    /**
     * byte转化为数组类型
     * @param b
     * @return
     */
    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum|(b[0] & 0xff) << 0;
        accum = accum|(b[1] & 0xff) << 8;
        accum = accum|(b[2] & 0xff) << 16;
        accum = accum|(b[3] & 0xff) << 24;
        return accum;
        // return Float.intBitsToFloat(accum);
    }

    /**
     * byte数组转化为16进制的字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }

}
