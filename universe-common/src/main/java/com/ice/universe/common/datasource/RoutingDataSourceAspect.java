package com.ice.universe.common.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

/**
 * @ClassName RoutingDataSourceAspect
 * @Description TODO
 * @Author liubin
 * @Date 2019/6/17 2:48 PM
 **/
@Aspect
public class RoutingDataSourceAspect implements Ordered {

    @Pointcut("@annotation(com.ice.universe.common.datasource.RoutingSelect)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        //如果当前是在事物环境下（这里还可以通过TransactionSynchronizationManager.isCurrentTransactionReadOnly()判断是否是只读事物做更细粒度控制）
        if(TransactionSynchronizationManager.isActualTransactionActive()){
            return pjp.proceed();
        }
        RoutingSelect routingSelect = getMethod(pjp).getAnnotation(RoutingSelect.class);
        if(routingSelect!=null){
            DatasourceHolder.setDatasourceType(routingSelect.dataSourceType());
            if(routingSelect.dataSourceType()==Routing.SLAVE&&routingSelect.dataSource()!=null){
                DatasourceHolder.setSlaveName(routingSelect.dataSource());
            }
        }
        return pjp.proceed();
    }

    protected Method getMethod(final JoinPoint jp) throws NoSuchMethodException {
        final Signature sig = jp.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalStateException("This annotation is only valid on a method.");
        }

        final MethodSignature msig = (MethodSignature)sig;
        final Object target = jp.getTarget();

        String name = msig.getName();
        Class<?>[] parameters = msig.getParameterTypes();

        return target.getClass().getMethod(name, parameters);
    }

    /**
     * 设定组件的优先级（小的优先）
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
