package com.zhong.concurrent;

/**
 * 原料商的抽象类
 *
 * @author 张中俊
 **/
abstract class AbstractMaterial implements Material,Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                material();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
