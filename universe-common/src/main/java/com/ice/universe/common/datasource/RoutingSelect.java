package com.ice.universe.common.datasource;

import java.lang.annotation.*;

/**
 * 数据源路由选择注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RoutingSelect {
    /**
     * 数据源类型
     * @return
     */
    Routing dataSourceType() default Routing.MASTER;

    /**
     * 指定数据源名称，可选
     * @return
     */
    String dataSource();
}
