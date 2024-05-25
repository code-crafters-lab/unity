package org.codecrafterslab.unity.dict.boot.app.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.codecrafterslab.unity.dict.boot.combine.ScopesOutputMode;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeScope;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;

    private String name;

    private Sex sex;

    @DictSerialize(SerializeScope.VALUE)
    private Sex valueSex;

    @DictSerialize(SerializeScope.LABEL)
    private Sex labelSex;

    @DictSerialize(SerializeScope.CODE)
    private Sex codeSex;

    @DictSerialize(value = SerializeScope.ALL, outputMode = ScopesOutputMode.FLAT)
    private Sex allSex;
}
