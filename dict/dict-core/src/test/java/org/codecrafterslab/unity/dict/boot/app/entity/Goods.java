package org.codecrafterslab.unity.dict.boot.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.codecrafterslab.unity.dict.boot.json.jackson.ser.SerializeScope;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DictSerialize(SerializeScope.VALUE)
public class Goods {

    @DictSerialize({SerializeScope.CODE_VALUE_LABEL, SerializeScope.CODE})
    private List<ProductService> services;

    private Integer id;

    private String name;

    @DictSerialize(scopes = {SerializeScope.LABEL, SerializeScope.CODE}, label = "name")
    private Sex sex;

    @DictSerialize(SerializeScope.LABEL)
    private ProductService service;

    private ProductService[] services2;
}
