package org.codecrafterslab.unity.dict.boot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codecrafterslab.unity.dict.boot.json.annotation.DictSerialize;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {

    private Integer id;

    private String name;

    private ProductService service;

    @DictSerialize({DictSerialize.Scope.CODE, DictSerialize.Scope.VALUE, DictSerialize.Scope.LABEL})
    private List<ProductService> services;
}
