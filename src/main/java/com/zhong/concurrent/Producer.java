package com.zhong.concurrent;
/**
 * 生产者的接口
 *
 * @author 张中俊
 */
public interface Producer {
    void produce() throws InterruptedException;
}
