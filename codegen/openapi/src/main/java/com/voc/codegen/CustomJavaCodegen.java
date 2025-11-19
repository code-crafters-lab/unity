package com.voc.codegen;

import com.google.auto.service.AutoService;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.languages.AbstractJavaCodegen;

@AutoService(CodegenConfig.class)
public class CustomJavaCodegen extends AbstractJavaCodegen {

    @Override
    public CodegenType getTag() {
        return super.getTag();
    }
}
