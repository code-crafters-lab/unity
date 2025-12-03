package org.codecrafterslab.unity.codegen.mybatis;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.TableConfiguration;

import java.util.List;
import java.util.Properties;

/**
 * 批量给实体类加后缀 PO（仅匹配指定前缀的表）
 */
public class EntitySuffixPlugin extends PluginAdapter {
    // 要添加的后缀
    private String suffix = "PO";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    // 读取配置文件中的参数（表前缀、后缀可自定义）
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (properties.containsKey("suffix")) {
            suffix = properties.getProperty("suffix");
        }
    }

    // 表初始化时，给匹配前缀的表加后缀
    @Override
    public void initialized(IntrospectedTable table) {
        FullyQualifiedTable fullyQualifiedTable = table.getFullyQualifiedTable();
//        FullyQualifiedTable qualifiedTable = new FullyQualifiedTable();
//        table.setFullyQualifiedTable(qualifiedTable);
//        fullyQualifiedTable.setDomainObjectName(fullyQualifiedTable.getDomainObjectName() + suffix);
        TableConfiguration tableConfiguration = table.getTableConfiguration();
        String tableName = tableConfiguration.getTableName();
        tableConfiguration.setDomainObjectName("BidInfoDO");
        if (!tableName.endsWith(suffix)) {
            String originalName = tableConfiguration.getDomainObjectName();
//            tableConfiguration.setDomainObjectName(originalName + suffix);
        }
    }

    @Override
    public boolean shouldGenerate(IntrospectedTable introspectedTable) {
        return super.shouldGenerate(introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type = topLevelClass.getType();
        String fullyQualifiedName = type.getFullyQualifiedName() + "DO";
        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(fullyQualifiedName);
//        topLevelClass.s(fullyQualifiedJavaType);
        String shortName = type.getShortName();
//        topLevelClass.setSuperClass(shortName + suffix);
//        topLevelClass.accept()
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
