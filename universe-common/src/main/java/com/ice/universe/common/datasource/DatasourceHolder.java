package com.ice.universe.common.datasource;

/**
 * 数据源路由相关参数
 * @ClassName DatasourceHolder
 * @Description TODO
 * @Author liubin
 * @Date 2019/6/14 5:10 PM
 **/
public class DatasourceHolder {
    private static final ThreadLocal<Boolean> TRANSACTION_BEGIN = new ThreadLocal<>();
    private static final ThreadLocal<Routing> DATASOURCE = new ThreadLocal<>();
    private static final ThreadLocal<String> SLAVE_NAME = new ThreadLocal<>();

    public static void markTransactionBegin(Boolean isTransactionBegin){
        TRANSACTION_BEGIN.set(isTransactionBegin);
    }

    public static void setDatasourceType(Routing routing){
        DATASOURCE.set(routing);
    }
    public static void setSlaveName(String slaveName){
        SLAVE_NAME.set(slaveName);
    }
    public static Boolean isTransactionBegin() {
        return TRANSACTION_BEGIN.get();
    }

    public static Routing getDatasource() {
        return DATASOURCE.get();
    }

    public static void remove(){
        TRANSACTION_BEGIN.set(null);
        DATASOURCE.set(null);
        SLAVE_NAME.set(null);
    }
}
