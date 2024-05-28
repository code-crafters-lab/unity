package org.codecrafterslab.unity.dict.boot.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.boot.DictProperties;
import org.codecrafterslab.unity.dict.boot.PersistenceMode;
import org.codecrafterslab.unity.dict.boot.handler.mybatis.EnumDictItemTypeHandler;
import org.codecrafterslab.unity.dict.boot.handler.mybatis.FuncEnumDictItemTypeHandler;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author WuYujie
 * @since 1.0.0
 */
@Configuration
@Import({MyBatisPlusTypeHandlerConfiguration.class})
public class TypeHandlerConfiguration {

    /**
     * 扩展修改 mybatis 配置
     *
     * @param provider       数据字典提供者
     * @param dictProperties 字典配置
     * @return Consumer<TypeHandlerRegistry>
     */
    @Bean
    Consumer<TypeHandlerRegistry> typeHandlerRegistryConsumer(EnumDictProvider provider,
                                                              DictProperties dictProperties) {
        return registry -> {
            /* 枚举字典 TypeHandler */
            registry.register(EnumDictItemTypeHandler.class);

            /* 功能点枚举字典 TypeHandler */
            register(registry, provider, dictProperties.getValuePersistenceMode());
        };
    }

    private void register(TypeHandlerRegistry registry, EnumDictProvider provider,
                          PersistenceMode persistenceMode) {
        List<Class<? super FuncEnumDictItem>> classes = provider.getFuncEnumDictItem();
        List<FuncEnumDictItemTypeHandler> typeHandlers = classes.stream()
                .map(clazz -> new FuncEnumDictItemTypeHandler(clazz, persistenceMode))
                .collect(Collectors.toList());
        typeHandlers.forEach(
                funcEnumDictItemTypeHandler -> {
                    registry.register(List.class, JdbcType.VARCHAR, funcEnumDictItemTypeHandler);
                    registry.register(List.class, JdbcType.JAVA_OBJECT, funcEnumDictItemTypeHandler);
                }
        );
    }

}
