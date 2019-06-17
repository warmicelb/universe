package com.ice.universe.common.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Random;

/**
 *  主从数据源路由（读写分离）
 * @ClassName RoutingDatasource
 * @Description TODO
 * @Author liubin
 * @Date 2019/6/14 4:33 PM
 **/
public class RoutingDatasource extends AbstractRoutingDataSource implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingDatasource.class);
    private static final Routing DEFAULT_ROUTING = Routing.MASTER;
    private DataSource master = null;
    private Map<String,DataSource> slaves = null;

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public void setSlaves(Map<String, DataSource> slaves) {
        this.slaves = slaves;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        Routing routing = DatasourceHolder.getDatasource();
        if(routing==null||DEFAULT_ROUTING==routing){
            return master;
        }else{
            //这里待优化
            DataSource dataSource = slaves.get(new Random().nextInt(slaves.size()));
            return dataSource==null?master:dataSource;
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        if(master==null){
            LOGGER.error("未设置主数据源,可能会导致项目出现异常");
        }
        if(slaves==null||slaves.size()==0){
            LOGGER.error("未设置从数据源");
        }
    }
}
