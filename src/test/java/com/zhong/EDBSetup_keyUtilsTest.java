package com.zhong;

import org.junit.Test;

/**
 * @author 张中俊
 * @create 2018-03-28 20:34
 **/
public class EDBSetup_keyUtilsTest {
    @Test
    public void getKeyTest() {
        MK mk = EDBSetup_keyUtils.getKey();
        System.out.println(mk);
    }
}
