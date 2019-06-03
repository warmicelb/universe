package com.ice.universe.common.lock;

import com.ice.universe.common.constant.Constant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName RedisLockAspect
 * @Description TODO 实现方法层的同步安全（通过redis分式式锁）
 * @Author liubin
 * @Date 2019/5/5 11:43 AM
 **/
@Aspect
@Component
public class RedisLockAspect {

    /**
     * 日志记录器.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockAspect.class);

    @Autowired
    private RedisSyncLock redisSyncLock;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut(value = "@annotation(com.ice.universe.common.lock.RedisLock)")
    public void syncPointcut() {
    }

    /**
     * 前置通知
     * -- 目标方法执行前执行.
     * @param pjp JoinPoint : 连接点
     */
    @Around(value = "syncPointcut()")
    public void around(ProceedingJoinPoint pjp) {
        Method method = getMethod(pjp);
        RedisLock annotation = method.getAnnotation(RedisLock.class);
        Map<String, Object> paramsNameMap = getParamsNameMap(method, pjp.getArgs());
        String key = concatLockKey(annotation.values(), paramsNameMap);
        String value = UUID.randomUUID().toString();
        try {
            if(redisSyncLock.tryLock(key,value, annotation.lockSeconds(), annotation.tryTimes())){
                pjp.proceed();
            }
        } catch (Throwable throwable) {
            LOGGER.error("同步方法执行出错",throwable);
        } finally {
            redisSyncLock.unlock(key,value);
        }
    }

    /**
     * 利用spel表达式进行lockKey的拼接
     * @param expression
     * @param paramsNameMap
     * @return
     */
    private String concatLockKey(String expression,Map<String, Object> paramsNameMap){
        //使用SpEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SpEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SpEL上下文中
        context.setVariables(paramsNameMap);
        return parser.parseExpression(Constant.REDIS_LOCK_PREFIX+expression).getValue(context,String.class);
    }


    /**
     * 获得切面的方法对象
     * @param jp
     * @return
     */
    private Method getMethod(JoinPoint jp) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        return method;
    }

    /**
     * 获取方法对象参数名和参数对象映射map
     * @param method
     * @param args
     * @return
     */
    public Map<String,Object> getParamsNameMap(Method method,Object[] args){
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Map<String,Object> paramsMap = new HashMap<>();
        if(args==null||args.length==0){
            return paramsMap;
        }
        for(int i=0;i<parameterNames.length;i++){
            paramsMap.put(parameterNames[i],args[i]);
        }
        return paramsMap;
    }

}
