package com.ice.universe.common.converter;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.sql.*;

/**
 * joda DateTime类型,jdbc Timestamp处理器
 * @author: ice
 * @create: 2018/12/26
 **/
//使用注解+扫描模式
//@MappedTypes(DateTime.class)
//@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class TimestampTypeHandler extends BaseTypeHandler<Timestamp> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Timestamp timestamp, JdbcType jdbcType) throws SQLException {
        preparedStatement.setTimestamp(i,timestamp);
    }

    @Override
    public Timestamp getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Timestamp sqlTimestamp = resultSet.getTimestamp(s);
        return sqlTimestamp != null ? sqlTimestamp : null;
    }

    @Override
    public Timestamp getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Timestamp sqlTimestamp = resultSet.getTimestamp(i);
        return sqlTimestamp != null ? sqlTimestamp : null;
    }

    @Override
    public Timestamp getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Timestamp sqlTimestamp = callableStatement.getTimestamp(i);
        return sqlTimestamp != null ? sqlTimestamp : null;
    }
}
