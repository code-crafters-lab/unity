package org.codecrafterslab.unity.response.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.codecrafterslab.unity.response.api.IPageResult;
import org.codecrafterslab.unity.response.api.IResult;
import org.codecrafterslab.unity.response.properties.ResultJsonProperties;
import org.codecrafterslab.unity.response.properties.ResponseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2021/04/25 13:27
 */
@Slf4j
public class ResultSerializer extends JsonSerializer<IResult<?>> {

    private final ResultJsonProperties property;

    public ResultSerializer(ResponseProperties resultProperties) {
        this.property = resultProperties.getResult();
    }

    @Override
    public void serialize(IResult result, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeBooleanField(property.getSuccess(), result.isSuccess());
        if (!ObjectUtils.isEmpty(result.getCode())) {
            gen.writeNumberField(property.getCode(), result.getCode());
        }
        if (!ObjectUtils.isEmpty(result.getMessage())) {
            gen.writeStringField(property.getMessage(), result.getMessage());
        }
        if (!ObjectUtils.isEmpty(result.getData())) {
            gen.writeObjectField(property.getData(), result.getData());
        }
        // 分页数据序列化
        if (result instanceof IPageResult) {
            Integer total = ((IPageResult<?>) result).getTotal();
            if (!ObjectUtils.isEmpty(total)) {
                gen.writeNumberField(property.getTotal(), total);
            }
        }
        gen.writeEndObject();
    }
}
