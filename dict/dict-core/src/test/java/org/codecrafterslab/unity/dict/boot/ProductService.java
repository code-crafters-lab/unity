package org.codecrafterslab.unity.dict.boot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;

@Getter
@AllArgsConstructor
public enum ProductService implements FuncEnumDictItem {

    HOME_DELIVERY("送货上门"),
    INSTALLATION_AND_DEBUGGING("安装调试"),
    TRAINING("培训"),
    SECONDARY_DEVELOPMENT("二次开发"),
    ;

    private final String name;

}
