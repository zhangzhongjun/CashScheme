package com.zhong.concurrent;

/**
 * 生产者的抽象类
 *
 * @author 张中俊
 **/
abstract class AbstractProducer implements Producer, Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

