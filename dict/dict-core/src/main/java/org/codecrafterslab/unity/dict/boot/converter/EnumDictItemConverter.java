package org.codecrafterslab.unity.dict.boot.converter;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * @author Wu Yujie
 * @since 0.2.0
 */
@Slf4j
public class EnumDictItemConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public EnumDictItemConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, EnumDictItem.class));
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Class<? extends EnumDictItem> target = (Class<? extends EnumDictItem>) targetType.getType();
        return EnumDictItem.find(target, source, val -> {
            /* 字符串类型 => 实际泛型的类型转换 */
            TypeDescriptor tagreTypeDescriptor = TypeDescriptor.valueOf(EnumDictItem.getValueType(target));
            Object converted = this.conversionService.convert(val, sourceType, tagreTypeDescriptor);
            if (log.isDebugEnabled()) {
                log.debug("{} : {} => {} : {}", sourceType, val, tagreTypeDescriptor, converted);
            }
            return converted;
        });
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return EnumDictItem.class.isAssignableFrom(targetType.getType());
    }

}
