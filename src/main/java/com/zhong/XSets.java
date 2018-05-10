package com.zhong;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * XSets 是索引的另外一部分
 *
 * @author 张中俊
 * @create 2018-03-18 17:11
 **/
public class XSets implements Serializable{
    /**
     * 里面的实际的值
     */
    ArrayList<SerializableElement> value = new ArrayList<>();

    public XSets(ArrayList<SerializableElement> value) {
        this.value = value;
    }

    public ArrayList<SerializableElement> getValue() {
        return value;
    }
}
