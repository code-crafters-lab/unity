package org.codecrafterslab.unity.dict.boot.converter;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Objects;
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
        /* 1. 先根据编码进行查找 */
        Class<? extends EnumDictItem> target = (Class<? extends EnumDictItem>) targetType.getType();
        EnumDictItem result = EnumDictItem.findByCode(target, (String) source);
        if (!Objects.isNull(result)) return result;

        /* 2. 字符串类型 => 实际泛型的类型转换 */
        TypeDescriptor tagreTypeDescriptor = TypeDescriptor.valueOf(EnumDictItem.getValueType(target));
        Object converted = this.conversionService.convert(source, sourceType, tagreTypeDescriptor);
        if (log.isDebugEnabled()) {
            log.debug("{} : {} => {} : {}", sourceType, source, tagreTypeDescriptor, converted);
        }
        /* 3. 根据实际值进行查找 */
        result = EnumDictItem.findByValue(target, converted);

        /* 4. 未转换成功则抛出业务异常 */
        if (Objects.isNull(result)) throw EnumDictItem.unsupported(target, source);
        return result;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return EnumDictItem.class.isAssignableFrom(targetType.getType());
    }

}
