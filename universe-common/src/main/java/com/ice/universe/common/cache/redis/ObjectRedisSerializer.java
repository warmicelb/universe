package com.ice.universe.common.cache.redis;

import com.ice.universe.common.exception.OpenException;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.Serializable;

/**
 * 自定义Object序列化器，用于value的序列化存储你
 * @author: ice
 * @create: 2018/12/20
 **/
public class ObjectRedisSerializer implements RedisSerializer {

    private static final Logger logger = LoggerFactory.getLogger(ObjectRedisSerializer.class);

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return o==null?null : SerializationUtils.serialize((Serializable) o);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            return bytes!=null&&bytes.length>0?SerializationUtils.deserialize(bytes):null;
        } catch (Exception e) {
            logger.error("redis反序列化失败"+e);
            throw new OpenException("redis反序列化失败");
        }
    }
}
