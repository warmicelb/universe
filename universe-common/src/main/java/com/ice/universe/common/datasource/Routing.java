package com.ice.universe.common.datasource;

/**
 * @ClassName Routing
 * @Description TODO
 * @Author liubin
 * @Date 2019/6/14 6:03 PM
 **/
public enum Routing {
    //主库
    MASTER,
    //从库
    SLAVE,
    //非事务下引用从库，在事务下引用主库
    AUTO;
}
