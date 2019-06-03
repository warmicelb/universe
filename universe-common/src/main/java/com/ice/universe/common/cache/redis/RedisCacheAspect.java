package com.ice.universe.common.cache.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisCacheAspect
 * @Description TODO
 * @Author liubin
 * @Date 2019/5/10 5:31 PM
 **/
@Component
public class RedisCacheAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 方法参数名列表帮助类
     */
    private DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut(value = "@annotation(com.ice.universe.common.cache.redis.RedisCache)")
    public void redisCache(){
    }

    @Around(value = "redisCache()")
    public Object cacheAround(final ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        RedisCache annotation = method.getAnnotation(RedisCache.class);
        Map<String, Object> parameterMap = getParameterMap(method, pjp.getArgs());
        String cacheKey = assembleCacheKey(annotation.value(), parameterMap);
        Object o = redisTemplate.opsForValue().get(cacheKey);
        if(o!=null){
            LOGGER.info("redis缓存有效，直接返回，key:{}",cacheKey);
            return o;
        }else{
            Object result = pjp.proceed();
            redisTemplate.opsForValue().set(cacheKey,result,annotation.expireSecond(), TimeUnit.SECONDS);
            return result;
        }
    }

    /**
     * 拼接缓存key
     * @param parameterMap
     */
    private String assembleCacheKey(String origin,Map<String, Object> parameterMap) {
        //_分割多个参数
        String SpEL = origin.replace("_", "+'_'+");
        //使用SpEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SpEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SpEL上下文中
        context.setVariables(parameterMap);
        return parser.parseExpression(SpEL).getValue(context, String.class);
    }


    /**
     * 获取方法
     * @param pjp
     * @return
     */
    public Method getMethod(ProceedingJoinPoint pjp){
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        return signature.getMethod();
    }

    /**
     * 获取方法参数map
     * @param method
     * @param args
     */
    private Map<String,Object> getParameterMap(Method method, Object[] args) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        HashMap<String,Object> parameterMap = new HashMap(16);
        if(parameterNames!=null){
            for(int i=0;i<parameterNames.length;i++){
                parameterMap.put(parameterNames[i],args[i]);
            }
        }
        return parameterMap;
    }
}
