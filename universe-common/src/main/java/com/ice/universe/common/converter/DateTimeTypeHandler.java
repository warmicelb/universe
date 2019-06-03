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
public class DateTimeTypeHandler extends BaseTypeHandler<DateTime> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DateTime dateTime, JdbcType jdbcType) throws SQLException {
        preparedStatement.setTimestamp(i,new Timestamp(dateTime.toDate().getTime()));
    }

    @Override
    public DateTime getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Timestamp sqlTimestamp = resultSet.getTimestamp(s);
        return sqlTimestamp != null ? new DateTime(sqlTimestamp.getTime()) : null;
    }

    @Override
    public DateTime getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Timestamp sqlTimestamp = resultSet.getTimestamp(i);
        return sqlTimestamp != null ? new DateTime(sqlTimestamp.getTime()) : null;
    }

    @Override
    public DateTime getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Timestamp sqlTimestamp = callableStatement.getTimestamp(i);
        return sqlTimestamp != null ? new DateTime(sqlTimestamp.getTime()) : null;
    }
}
