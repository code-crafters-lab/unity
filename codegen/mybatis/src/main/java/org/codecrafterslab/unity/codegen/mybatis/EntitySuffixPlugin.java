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
    public static final String ID_UNDERLINE = "id_";
    public static final String RECORD_ID = "recordId";

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

        this.modelSuffix = properties.getProperty(PRO_MODEL_SUFFIX, "DO");
        this.clientSuffix = properties.getProperty(PRO_CLIENT_SUFFIX);
        this.exampleSuffix = properties.getProperty(PRO_EXAMPLE_SUFFIX);
    }

    // 表初始化时，给匹配前缀的表加后缀
    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        fixedAliasableSqlTableName(introspectedTable);

//        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
//        introspectedTable.setFullyQualifiedTable(fullyQualifiedTable);
//        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
//        introspectedTable.setTableConfiguration(tableConfiguration);

//        String tableReplaceDisable = tableConfiguration.getProperty(PRO_TABLE_REPLACE_DISABLE);
//        this.tableReplaceDisable = tableReplaceDisable != null && StringUtility.isTrue(tableReplaceDisable);
//        String columnReplaceDisable = tableConfiguration.getProperty(PRO_COLUMN_REPLACE_DISABLE);
//        this.columnReplaceDisable = columnReplaceDisable != null && StringUtility.isTrue(columnReplaceDisable);


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
//         ---------------------- 后缀修正 -------------------------------

        // 3. model
        addModelSuffix(introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        renameIdUnderline(method);
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        renameIdUnderline(method);
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    private static void renameIdUnderline(org.mybatis.generator.api.dom.java.Method method) {
        List<Parameter> parameters = method.getParameters();
        boolean found = false;
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            if (ID_UNDERLINE.equals(parameter.getName())) {
                found = true;
                parameters.set(i, new Parameter(parameter.getType(), RECORD_ID, parameter.isVarargs()));
            }
        }
        for (int i = 0; i < method.getBodyLines().size() && found; i++) {
            String line = method.getBodyLines().get(i);
            if (line.contains(ID_UNDERLINE)) {
                method.getBodyLines().set(i, line.replace(ID_UNDERLINE, RECORD_ID));
            }
        }
    }

    private static void fixedAliasableSqlTableName(IntrospectedTable introspectedTable) {
        String fullyQualifiedTableNameAtRuntime = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        if (fullyQualifiedTableNameAtRuntime.contains(".")) {
            LinkedList<String> nameList = new LinkedList<>(Arrays.asList(fullyQualifiedTableNameAtRuntime.split("\\.")));
            String tableName = nameList.getLast();
            if (!StringUtility.stringHasValue(tableName)) {
                nameList.clear();
                return;
            }
            introspectedTable.setSqlMapFullyQualifiedRuntimeTableName(tableName);
        }
    }

    private void addModelSuffix(IntrospectedTable introspectedTable) {
        if (!StringUtility.stringHasValue(modelSuffix)) return;
        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();

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

        StringBuilder sb = new StringBuilder();
        sb.append(javaModelPackage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Key");
        sb.append(this.modelSuffix);
        introspectedTable.setPrimaryKeyType(sb.toString());

        sb.setLength(0);
        sb.append(javaModelPackage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append(this.modelSuffix);
        introspectedTable.setBaseRecordType(sb.toString());

        sb.setLength(0);
        sb.append(javaModelPackage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("WithBLOBs");
        sb.append(this.modelSuffix);
        introspectedTable.setRecordWithBLOBsType(sb.toString());
    }

}
