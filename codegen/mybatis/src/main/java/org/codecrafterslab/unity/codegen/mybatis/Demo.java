package org.codecrafterslab.unity.codegen.mybatis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
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
        InputStream configFile = Demo.class.getResourceAsStream("/generatorConfig.xml");
        String base = getJarPath(Demo.class);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
//        Configuration config = new Configuration();
        for (Context context : config.getContexts()) {
            JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
            String targetProject = javaClientGeneratorConfiguration.getTargetProject();
            targetProject = String.join(File.separator, base, targetProject);
            javaClientGeneratorConfiguration.setTargetProject(targetProject);

            JavaModelGeneratorConfiguration modelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
            String targetProject2 = modelGeneratorConfiguration.getTargetProject();
            targetProject2 = String.join(File.separator, base, targetProject2);
            modelGeneratorConfiguration.setTargetProject(targetProject2);

        }

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        for (String warning : warnings) {
            System.out.println(warning);
        }

    }

}
