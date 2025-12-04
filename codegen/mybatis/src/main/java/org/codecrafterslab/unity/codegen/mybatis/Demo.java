package org.codecrafterslab.unity.codegen.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Demo {

    /**
     * 获取当前 JAR 包所在的文件路径（或 IDE 中 classes 目录路径）
     *
     * @param clazz 当前项目中的任意一个类（推荐用当前工具类本身）
     * @return JAR 包路径（如 D:/app/my-project.jar）或 classes 目录路径（如 D:/project/target/classes/）
     */
    public static String getJarPath(Class<?> clazz) throws Exception {
        // 1. 获取类的保护域（包含类的位置信息）
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URL location = codeSource.getLocation();

        // 2. 转换为 URI（处理路径中的空格、特殊字符）
        URI uri = location.toURI();

        // 3. 转为 File 对象，获取路径
        File file = new File(uri);
        String path = file.getAbsolutePath();

        // 4. 处理：如果是 JAR 包，返回 JAR 文件路径；如果是 IDE 运行，返回 classes 目录路径
        return path.replace("/build/classes/java/main", "");
    }

    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;

        String base = getJarPath(Demo.class);
        InputStream configFile = Demo.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);

        for (Context context : config.getContexts()) {
            JavaClientGeneratorConfiguration clientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
            String targetProject = clientGeneratorConfiguration.getTargetProject();
            targetProject = String.join(File.separator, base, targetProject);
            clientGeneratorConfiguration.setTargetProject(targetProject);

            JavaModelGeneratorConfiguration modelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
            String targetProject2 = modelGeneratorConfiguration.getTargetProject();
            targetProject2 = String.join(File.separator, base, targetProject2);
            modelGeneratorConfiguration.setTargetProject(targetProject2);
        }

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        for (String warning : warnings) {
            log.warn(warning);
        }

    }

    private Configuration calcDynamicConfiguration(String base) {
        Configuration config = new Configuration();
        Context context = new Context(ModelType.HIERARCHICAL);
        context.addProperty("javaFormatter", "org.codecrafterslab.unity.codegen.mybatis.CustomJavaFormatter");
        config.addContext(context);

        context.setId("mysql");
        context.setTargetRuntime("MyBatis3DynamicSql");
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("org.codecrafterslab.unity.codegen.mybatis.EntitySuffixPlugin");
        context.addPluginConfiguration(pluginConfiguration);

        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.addProperty("useLegacyGeneratedAnnotation", "true");
        commentGeneratorConfiguration.addProperty("addRemarkComments", "true");
        commentGeneratorConfiguration.addProperty("dateFormat", "yyyy-MM-dd HH:mm:ss");
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass("com.mysql.cj.jdbc.Driver");
        jdbcConnectionConfiguration.setConnectionURL("jdbc:mysql://localhost:3306/cds_infra?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        jdbcConnectionConfiguration.setUserId("root");
        jdbcConnectionConfiguration.setPassword("root!@@&");
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        javaTypeResolverConfiguration.addProperty("useJSR310Types", "true");
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(String.join(File.separator, base, "src", "main", "java"));
        javaModelGeneratorConfiguration.setTargetPackage("net.jqsoft.cds.bid.model");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);


        JavaClientGeneratorConfiguration clientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        clientGeneratorConfiguration.setTargetProject(String.join(File.separator, base, "src", "main", "java"));
        clientGeneratorConfiguration.setTargetPackage("net.jqsoft.cds.bid.mapper");
        clientGeneratorConfiguration.addProperty("dynamicSqlSupportPackage", "net.jqsoft.cds.bid.mapper.sql");
        context.setJavaClientGeneratorConfiguration(clientGeneratorConfiguration);


        TableConfiguration tc = new TableConfiguration(context);
        tc.setCatalog("cds_infra");
        tc.setTableName("bid_info");
        tc.setAlias("i");
        tc.setDomainObjectName("BidInfoRecord");
        context.addTableConfiguration(tc);


        return config;

    }
}
