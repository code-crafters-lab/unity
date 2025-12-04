package org.codecrafterslab.unity.codegen.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 批量给实体类加后缀 PO（仅匹配指定前缀的表）
 */
@Slf4j
public class EntitySuffixPlugin extends PluginAdapter {

    public static final String PRO_TABLE_SEARCH_STRING = "domainObjectRenamingRule.searchString";  // 查找 property
    public static final String PRO_TABLE_REPLACE_STRING = "domainObjectRenamingRule.replaceString";  // 替换 property
    public static final String PRO_TABLE_REPLACE_DISABLE = "domainObjectRenamingRule.disable";  // 替换 property
    public static final String PRO_COLUMN_SEARCH_STRING = "columnRenamingRule.searchString";  // 查找 property
    public static final String PRO_COLUMN_REPLACE_STRING = "columnRenamingRule.replaceString";  // 替换 property
    public static final String PRO_COLUMN_REPLACE_DISABLE = "columnRenamingRule.disable";  // 替换 property

    public static final String PRO_MODEL_SUFFIX = "modelSuffix"; // model 结尾
    public static final String PRO_CLIENT_SUFFIX = "clientSuffix";  // client 结尾
    public static final String PRO_EXAMPLE_SUFFIX = "exampleSuffix"; // example 结尾

    private String tableSearchString;
    private String tableReplaceString;
    private boolean tableReplaceDisable;
    private String columnSearchString;
    private String columnReplaceString;
    private boolean columnReplaceDisable;

    private String modelSuffix; // model 结尾
    private String clientSuffix;  // client 结尾
    private String exampleSuffix; // example 结尾

    @Override
    public boolean validate(List<String> warnings) {
//        Properties properties = this.getProperties();


        return true;
    }

    // 读取配置文件中的参数（表前缀、后缀可自定义）
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.tableSearchString = properties.getProperty(PRO_TABLE_SEARCH_STRING);
        this.tableReplaceString = properties.getProperty(PRO_TABLE_REPLACE_STRING);
        this.columnSearchString = properties.getProperty(PRO_COLUMN_SEARCH_STRING);
        this.columnReplaceString = properties.getProperty(PRO_COLUMN_REPLACE_STRING);  // 和官方行为保持一致

        this.modelSuffix = properties.getProperty(PRO_MODEL_SUFFIX);
        this.clientSuffix = properties.getProperty(PRO_CLIENT_SUFFIX);
        this.exampleSuffix = properties.getProperty(PRO_EXAMPLE_SUFFIX);
//        if (properties.containsKey("suffix")) {
//            suffix = properties.getProperty("suffix");
//        }
    }

    // 表初始化时，给匹配前缀的表加后缀
    @Override
    public void initialized(IntrospectedTable introspectedTable) {

        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        introspectedTable.setFullyQualifiedTable(fullyQualifiedTable);
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        introspectedTable.setTableConfiguration(tableConfiguration);
//        FullyQualifiedTable qualifiedTable = new FullyQualifiedTable();
//        table.setFullyQualifiedTable(qualifiedTable);
//        String modelName = String.format("%s%s", fullyQualifiedTable.getDomainObjectName(), suffix);
//        introspectedTable.setBaseRecordType("modelName");
//        String mapper = String.format("net.jqsoft.cds.bid.mapper.%sMapper", "BidInfo");
//        table.setMyBatis3JavaMapperType(mapper);
//        String dynamicSql = String.format("net.jqsoft.cds.bid.mapper.sql.%sDynamicSqlSupport", "BidInfo");
//        table.setMyBatisDynamicSqlSupportType(dynamicSql);
//        table.setMyBatisDynamicSQLTableObjectName("Table");
//        fullyQualifiedTable.setDomainObjectName(fullyQualifiedTable.getDomainObjectName() + suffix);


        String tableReplaceDisable = tableConfiguration.getProperty(PRO_TABLE_REPLACE_DISABLE);
        this.tableReplaceDisable = tableReplaceDisable != null && StringUtility.isTrue(tableReplaceDisable);
        String columnReplaceDisable = tableConfiguration.getProperty(PRO_COLUMN_REPLACE_DISABLE);
        this.columnReplaceDisable = columnReplaceDisable != null && StringUtility.isTrue(columnReplaceDisable);

        Method calculateJavaModelPackageMethod = ReflectionUtils.findMethod(IntrospectedTable.class, "calculateJavaModelPackage");
        assert calculateJavaModelPackageMethod != null;
        calculateJavaModelPackageMethod.setAccessible(true);
        String javaModelPackage = (String) ReflectionUtils.invokeMethod(calculateJavaModelPackageMethod, introspectedTable);
        log.info("javaModelPackage: {}", javaModelPackage);

        Method calculateJavaClientInterfacePackageMethod = ReflectionUtils.findMethod(IntrospectedTable.class, "calculateJavaClientInterfacePackage");
        assert calculateJavaClientInterfacePackageMethod != null;
        calculateJavaClientInterfacePackageMethod.setAccessible(true);
        String javaClientInterfacePackage = (String) ReflectionUtils.invokeMethod(calculateJavaClientInterfacePackageMethod, introspectedTable);
        log.info("javaClientInterfacePackage: {}", javaClientInterfacePackage);

        Method calculateSqlMapPackageMethod = ReflectionUtils.findMethod(IntrospectedTable.class, "calculateSqlMapPackage");
        assert calculateSqlMapPackageMethod != null;
        calculateSqlMapPackageMethod.setAccessible(true);
        String sqlMapPackage = (String) ReflectionUtils.invokeMethod(calculateSqlMapPackageMethod, introspectedTable);
        log.info("sqlMapPackage: {}", sqlMapPackage);

        //        // --------------------- table 重命名 ----------------------------
//        if (tableConfiguration.getDomainObjectRenamingRule() == null
//                && this.tableSearchString != null && !this.tableReplaceDisable) {
//            DomainObjectRenamingRule rule = new DomainObjectRenamingRule();
//            rule.setSearchString(this.tableSearchString);
//            rule.setReplaceString(this.tableReplaceString);
//            tableConfiguration.setDomainObjectRenamingRule(rule);
//            BeanUtils.setProperty(introspectedTable.getFullyQualifiedTable(), "domainObjectRenamingRule", rule);
//
//            // 重新初始化一下属性
//            BeanUtils.invoke(introspectedTable, IntrospectedTable.class, "calculateJavaClientAttributes");
//            BeanUtils.invoke(introspectedTable, IntrospectedTable.class, "calculateModelAttributes");
//            BeanUtils.invoke(introspectedTable, IntrospectedTable.class, "calculateXmlAttributes");
//        }
//
//        // --------------------- column 重命名 ---------------------------
//        if (tableConfiguration.getColumnRenamingRule() == null
//                && this.columnSearchString != null && !this.columnReplaceDisable) {
//            ColumnRenamingRule rule = new ColumnRenamingRule();
//            rule.setSearchString(this.columnSearchString);
//            rule.setReplaceString(this.columnReplaceString);
//            tableConfiguration.setColumnRenamingRule(rule);
//
//            // introspectedTable 使用到的所有column过滤并替换
//            this.renameColumns(introspectedTable.getAllColumns(), rule, tableConfiguration);
//        }
//
//        // ---------------------- 后缀修正 -------------------------------
//        // 1. client
//        if (this.clientSuffix != null) {
//            // mapper
//            StringBuilder sb = new StringBuilder();
//            sb.append(javaClientInterfacePackage);
//            sb.append('.');
//            if (stringHasValue(tableConfiguration.getMapperName())) {
//                sb.append(tableConfiguration.getMapperName());
//            } else {
//                if (stringHasValue(fullyQualifiedTable.getDomainObjectSubPackage())) {
//                    sb.append(fullyQualifiedTable.getDomainObjectSubPackage());
//                    sb.append('.');
//                }
//                sb.append(fullyQualifiedTable.getDomainObjectName());
//                sb.append(this.clientSuffix);
//            }
//            introspectedTable.setMyBatis3JavaMapperType(sb.toString());
//            // xml mapper namespace
//            sb.setLength(0);
//            sb.append(sqlMapPackage);
//            sb.append('.');
//            if (stringHasValue(tableConfiguration.getMapperName())) {
//                sb.append(tableConfiguration.getMapperName());
//            } else {
//                sb.append(fullyQualifiedTable.getDomainObjectName());
//                sb.append(this.clientSuffix);
//            }
//            introspectedTable.setMyBatis3FallbackSqlMapNamespace(sb.toString());
//            // xml file
//            sb.setLength(0);
//            sb.append(fullyQualifiedTable.getDomainObjectName());
//            sb.append(this.clientSuffix);
//            sb.append(".xml");
//
//            introspectedTable.setMyBatis3XmlMapperFileName(sb.toString());
//        }
//        // 2. example
//        if (this.exampleSuffix != null) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(javaModelPackage);
//            sb.append('.');
//            sb.append(fullyQualifiedTable.getDomainObjectName());
//            sb.append(this.exampleSuffix);
//            introspectedTable.setExampleType(sb.toString());
//        }
//        // 3. model
//        if (this.modelSuffix != null) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(javaModelPackage);
//            sb.append('.');
//            sb.append(fullyQualifiedTable.getDomainObjectName());
//            sb.append("Key");
//            sb.append(this.modelSuffix);
//            introspectedTable.setPrimaryKeyType(sb.toString());
//
//            sb.setLength(0);
//            sb.append(javaModelPackage);
//            sb.append('.');
//            sb.append(fullyQualifiedTable.getDomainObjectName());
//            sb.append(this.modelSuffix);
//            introspectedTable.setBaseRecordType(sb.toString());
//
//            sb.setLength(0);
//            sb.append(javaModelPackage);
//            sb.append('.');
//            sb.append(fullyQualifiedTable.getDomainObjectName());
//            sb.append("WithBLOBs");
//            sb.append(this.modelSuffix);
//            introspectedTable.setRecordWithBLOBsType(sb.toString());
//        }

    }

    @Override
    public boolean shouldGenerate(IntrospectedTable introspectedTable) {
        return super.shouldGenerate(introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean dynamicSqlSupportGenerated(TopLevelClass supportClass, IntrospectedTable introspectedTable) {
        List<InnerClass> innerClasses = supportClass.getInnerClasses();
        Pattern pattern = Pattern.compile("super\\(\"(.*)\", (.*)\\)");
        LinkedList<String> nameList = new LinkedList<>();
        for (InnerClass innerClass : innerClasses) {
            for (int j = 0; j < innerClass.getMethods().size(); j++) {
                org.mybatis.generator.api.dom.java.Method method = innerClass.getMethods().get(j);
                if (!method.isConstructor()) continue;
                List<String> bodyLines = method.getBodyLines();
                for (int k = 0; k < bodyLines.size(); k++) {
                    String line = bodyLines.get(k);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String tableName = matcher.group(1);
                        nameList.addAll(Arrays.asList(tableName.split("\\.")));
                        tableName = nameList.getLast();
                        if (!StringUtility.stringHasValue(tableName)) {
                            nameList.clear();
                            continue;
                        }
                        String constructor = matcher.group(2);
                        String result = String.format("super(\"%s\", %s);", tableName, constructor);
                        bodyLines.set(k, result);
                    }
                }
            }
        }
        return super.dynamicSqlSupportGenerated(supportClass, introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        List<Parameter> parameters = method.getParameters();
        boolean found = false;
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            if ("id_".equals(parameter.getName())) {
                found = true;
                parameters.set(i, new Parameter(parameter.getType(), "idValue", parameter.isVarargs()));
            }
        }
        for (int i = 0; i < method.getBodyLines().size() && found; i++) {
            String line = method.getBodyLines().get(i);
            if (line.contains("id_")) {
                method.getBodyLines().set(i, line.replace("id_", "idValue"));
            }
        }
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // id_ -> idValue
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }
}
