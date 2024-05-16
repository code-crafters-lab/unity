package org.codecrafterslab.unity.dict.boot.handler.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据字典项类型处理器
 *
 * @author Wu Yujie
 * @see TypeHandlerRegistry#register(Class)
 */
public abstract class AbstractDictionaryItemTypeHandler<T> extends BaseTypeHandler<Object> {
    protected final Class<T> type;

    public AbstractDictionaryItemTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setObject(i, fromDictionaryItem(parameter));
        } else {
            ps.setObject(i, fromDictionaryItem(parameter), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Object object = resultSet.getObject(columnName);
        return toDictionaryItem(object);
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Object object = resultSet.getObject(i);
        return toDictionaryItem(object);
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Object object = callableStatement.getObject(i);
        return toDictionaryItem(object);
    }

    protected abstract Object fromDictionaryItem(Object parameter);

    protected abstract T toDictionaryItem(Object value);

}
