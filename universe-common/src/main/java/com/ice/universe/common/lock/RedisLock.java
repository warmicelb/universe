package com.ice.universe.common.lock;

import java.lang.annotation.*;

/**
 * @ClassName Sync
 * @Description TODO 被{@link RedisLock}注解的方法,通过设置{@link #values()}作为锁,在被调用时会被同步,可以解决分布式并发系列的问题。
 * @Author liubin
 * @Date 2019/5/5 2:15 PM
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisLock {
    /**
     * 获取锁的尝试次数
     * @return
     */
    int tryTimes() default 1;
    //锁周期
    int lockSeconds() default 10;
    //key的个性化属性
    String values() default "";
}
