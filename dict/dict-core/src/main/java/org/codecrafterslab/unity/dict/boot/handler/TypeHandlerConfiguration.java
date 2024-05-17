package org.codecrafterslab.unity.dict.boot.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.boot.handler.mybatis.EnumDictItemTypeHandler;
import org.codecrafterslab.unity.dict.boot.handler.mybatis.FuncEnumDictItemTypeHandler;
import org.codecrafterslab.unity.dict.boot.handler.provider.FuncEnumDictItemProvider;
import org.codecrafterslab.unity.dict.boot.provider.EnumDictItemProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WuYujie
 * @since 1.0.0
 */
@Configuration
@Import({MyBatisPlusTypeHandlerConfiguration.class})
public class TypeHandlerConfiguration {

    @Bean
    EnumDictItemProvider.Builder enumProviderBuilder(ObjectProvider<EnumDictItemProvider> providers) {
        return new EnumDictItemProvider.Builder(providers);
    }

    @Bean
    FuncEnumDictItemProvider.Builder funcEnumProviderBuilder(ObjectProvider<FuncEnumDictItemProvider> providers) {
        return new FuncEnumDictItemProvider.Builder(providers);
    }

    @Bean
    Consumer<TypeHandlerRegistry> typeHandlerRegistryConsumer(FuncEnumDictItemProvider.Builder builder) {
        return registry -> {
            /* 枚举字典 TypeHandler */
            registry.register(EnumDictItemTypeHandler.class);

            /* 功能点枚举字典 TypeHandler */
            register(registry, builder);
        };
    }

    private void register(TypeHandlerRegistry registry, FuncEnumDictItemProvider.Builder builder) {
        registry.register(List.class, FuncEnumDictItemTypeHandler.class);

        Collection<Class<? extends FuncEnumDictItem>> classes = builder.get();
        List<FuncEnumDictItemTypeHandler<? extends FuncEnumDictItem>> typeHandlers =
                classes.stream().map((Function<Class<? extends FuncEnumDictItem>, FuncEnumDictItemTypeHandler<?
                                extends FuncEnumDictItem>>) FuncEnumDictItemTypeHandler::new)
                        .collect(Collectors.toList());
        typeHandlers.forEach(funcEnumDictItemTypeHandler -> {
                    registry.register(List.class, JdbcType.VARCHAR, funcEnumDictItemTypeHandler);
                    registry.register(List.class, JdbcType.JAVA_OBJECT, funcEnumDictItemTypeHandler);
                }

        );
    }

}
