package com.zhong;

/**
 * @author 张中俊
 * @create 2018-03-30 11:36
 **/

import java.io.Serializable;
import java.util.Arrays;

/**
 * 包括一个密文 和 一个元素
 */
public class t_item implements Serializable {
    /**
     * 密文
     */
    byte [] e;
    /**
     * 一个元素，相当于是一个索引
     */
    SerializableElement y;

    public t_item(byte[] e, SerializableElement y) {
        this.e = e;
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("e: " + Arrays.toString(e));
        sb.append("y: " + y.getElement().toString());
        return sb.toString();
    }

    public byte[] getE() {
        return e;
    }

    public SerializableElement getY() {
        return y;
    }
}