package org.codecrafterslab.unity.dict.boot;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;

    private String name;

    private Sex sex;

    private Sex valueSex;

    private Sex labelSex;

    private Sex codeSex;

    private Sex allSex;
}
