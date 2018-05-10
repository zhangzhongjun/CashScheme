package com.zhong.concurrent;

/**
 * 不同的模型实现中，生产者、消费者的具体实现也不同，所以需要为模型定义抽象工厂方法
 *
 * @author 张中俊
 */
public interface Model {
    /**
     * 用户自定义的消费者的线程
     *
     * @return 消费者的线程
     */
    Runnable newRunnableConsumer();

    /**
     * 用户自定义的生产者线程
     *
     * @return 生产者的线程
     */
    Runnable newRunnableProducer();

    /**
     * 用户自定义的原料线程
     *
     * @return 原料商的线程
     */
    Runnable newRunnableMaterial();
}
