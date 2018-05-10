package com.zhong.concurrent;
/**
 * 消费者的接口
 *
 * @author 张中俊
 */
public interface Consumer {
    void consume() throws InterruptedException;
}
