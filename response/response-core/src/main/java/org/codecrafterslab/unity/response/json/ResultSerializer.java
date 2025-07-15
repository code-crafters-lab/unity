package org.codecrafterslab.unity.response.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.codecrafterslab.unity.response.api.IPageResult;
import org.codecrafterslab.unity.response.api.IResult;
import org.codecrafterslab.unity.response.api.ISummaryResult;
import org.codecrafterslab.unity.response.properties.ResultJsonProperties;
import org.codecrafterslab.unity.response.properties.ResponseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2021/04/25 13:27
 */
@Slf4j
public class ResultSerializer<T extends IResult<?>> extends JsonSerializer<T> {

    private final ResultJsonProperties property;
    private final Class<T> aClass;

    @SuppressWarnings("unchecked")
    public ResultSerializer(ResponseProperties resultProperties) {
        this.property = resultProperties.getResult();
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(IResult.class, ResolvableType.forClass(Object.class));
        aClass = (Class<T>) resolvableType.resolve();
    }

    @Override
    public void serialize(T result, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeBooleanField(property.getSuccess(), result.isSuccess());
        /* 非默认成功编码则输出 */
        Long code = Long.parseLong(String.valueOf(BizStatus.OK.getCode()));;
        if (!ObjectUtils.isEmpty(result.getCode()) && !result.getCode().equals(code)) {
            gen.writeNumberField(property.getCode(), result.getCode());
        }
        /* 非默认成功信息则输出 */
        if (!ObjectUtils.isEmpty(result.getMessage()) && !BizStatus.OK.getMessage().equalsIgnoreCase(result.getMessage())) {
            gen.writeStringField(property.getMessage(), result.getMessage());
        }
        if (!ObjectUtils.isEmpty(result.getData())) {
            gen.writeObjectField(property.getData(), result.getData());
        }
        /* 汇总数据序列化输出 */
        if (result instanceof ISummaryResult) {
            Object summary = ((ISummaryResult<?, ?>) result).getSummary();
            if (!ObjectUtils.isEmpty(summary)) {
                gen.writeObjectField(property.getSummary(), summary);
            }
        }
        /* 数据总数序列化输出 */
        if (result instanceof IPageResult) {
            Integer total = ((IPageResult<?, ?>) result).getTotal();
            if (!ObjectUtils.isEmpty(total)) {
                gen.writeNumberField(property.getTotal(), total);
            }
        }
        gen.writeEndObject();
    }

    @Override
    public Class<T> handledType() {
        return aClass;
    }
}
