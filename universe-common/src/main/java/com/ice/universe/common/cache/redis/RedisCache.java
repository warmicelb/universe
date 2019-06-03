package com.ice.universe.common.cache.redis;

public @interface RedisCache {
    /**
     * 缓存的key特征值
     * @return
     */
    String value() default "";

    /**
     * 默认缓存时间1个小时
     * @return
     */
    long expireSecond() default 1*60*60;
}
