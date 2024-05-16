package org.codecrafterslab.unity.dict.boot.handler.mybatis;

import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.codecrafterslab.unity.dict.api.EnumDictItem;

/**
 * mybatis EnumDictItem 枚举类型处理器
 *
 * @author Wu Yujie
 * @see EnumDictItem
 * @see TypeHandlerRegistry#register(Class)
 */
@MappedTypes(EnumDictItem.class)
public class EnumDictItemTypeHandler<T extends EnumDictItem<?>> extends AbstractDictionaryItemTypeHandler<T> {

    public EnumDictItemTypeHandler(Class<T> type) {
        super(type);
    }

    @Override
    protected Object fromDictionaryItem(Object parameter) {
        if (parameter instanceof EnumDictItem) {
            return ((EnumDictItem<?>) parameter).getValue();
        }
        return null;
    }

    @Override
    protected T toDictionaryItem(Object value) {
        return EnumDictItem.find(type, value);
    }

}
