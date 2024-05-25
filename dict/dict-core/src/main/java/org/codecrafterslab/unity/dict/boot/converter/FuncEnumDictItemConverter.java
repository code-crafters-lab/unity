package org.codecrafterslab.unity.dict.boot.converter;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.api.func.Functions;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

/**
 * @author Wu Yujie
 * @since 1.0.0
 */
@Slf4j
public class FuncEnumDictItemConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public FuncEnumDictItemConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return new HashSet<>(Arrays.asList(
                new ConvertiblePair(String.class, FuncEnumDictItem[].class),
                new ConvertiblePair(String.class, Collection.class)
        ));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Object convert(@Nullable Object source, TypeDescriptor sourceType,
                          TypeDescriptor targetType) {
        Class<? extends FuncEnumDictItem> target =
                (Class<? extends FuncEnumDictItem>) Objects.requireNonNull(targetType.getElementTypeDescriptor()).getType();

        /* 1. String => BigInteger */
        TypeDescriptor targetTypeDescriptor = TypeDescriptor.valueOf(BigInteger.class);
        BigInteger converted = (BigInteger) this.conversionService.convert(source, sourceType, targetTypeDescriptor);
        if (log.isTraceEnabled()) {
            log.trace("convert {}({}) to {}({})", sourceType, source, targetTypeDescriptor, converted);
        }

        /* 2. BigInteger => Functions */
        Functions functions = Functions.builder().of(converted).build();
        List<? extends FuncEnumDictItem> items = FuncEnumDictItem.find(target, functions);

        /* 3. 数组类型返回 */
        Class<?> containerType = targetType.getType();
        if (containerType.isArray()) {
            return items.toArray((FuncEnumDictItem[]) Array.newInstance(target, items.size()));
        }

        /* 4. 集合类型返回 */
        TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
        Collection<Object> result = CollectionFactory.createCollection(targetType.getType(), elementDesc != null ?
                elementDesc.getType() : null, items.size());
        result.addAll(items);
        return result;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        TypeDescriptor elementTypeDescriptor = targetType.getElementTypeDescriptor();
        return elementTypeDescriptor != null && FuncEnumDictItem.class.isAssignableFrom(elementTypeDescriptor.getType());
    }

}
