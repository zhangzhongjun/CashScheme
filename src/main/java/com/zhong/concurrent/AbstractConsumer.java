package com.zhong.concurrent;

/**
 * 消费者的抽象类
 *
 * @author 张中俊
 **/
abstract class AbstractConsumer implements Consumer, Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}