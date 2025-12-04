package org.codecrafterslab.unity.codegen.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.TopLevelEnumeration;

import java.util.Properties;

/**
 * MBG 格式化插件：基于 google-java-format 自动格式化生成的 Java 代码
 * 无需修改模板，拦截生成过程直接格式化
 */
@Slf4j
public class CustomJavaFormatter extends DefaultJavaFormatter implements JavaFormatter {

    @Override
    public String visit(TopLevelClass topLevelClass) {
        String formatted = super.visit(topLevelClass);
        return formatCode(formatted);
    }

    @Override
    public String visit(TopLevelEnumeration topLevelEnumeration) {
        String visit = super.visit(topLevelEnumeration);
        return formatCode(visit);
    }

    @Override
    public String visit(Interface topLevelInterface) {
        String visit = super.visit(topLevelInterface);
        return formatCode(visit);
    }

    protected String formatCode(String rawCode) {
        // 1. 配置格式化规则（完全匹配团队规范，示例为 4 空格缩进+通用规范）
        Properties formatterConfig = getProperties();

        // 2. 初始化 Eclipse 格式化器（无任何 JDK 内部 API 依赖）
        CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(formatterConfig);

        // 步骤2：Eclipse 核心格式化逻辑（适配所有 JDK 版本）
        TextEdit formatEdit = codeFormatter.format(
                CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS, // 格式化整个 Java 文件
                rawCode,                           // 原始代码
                0,                                 // 起始偏移量
                rawCode.length(),                  // 格式化长度
                0,                                 // 缩进空格数
                System.lineSeparator()             // 系统换行符（LF/CRLF）
        );

        // 步骤3：应用格式化结果
        IDocument document = new Document(rawCode);
        try {
            formatEdit.apply(document);
            return document.get();
        } catch (BadLocationException e) {
            if (log.isErrorEnabled()) {
                log.error("格式化代码失败: {}", e.getLocalizedMessage());
            }
        }

        return rawCode;
    }

    private static Properties getProperties() {
        Properties formatterConfig = new Properties();
        // 核心规则：4 空格缩进（可改为 2 空格）
        formatterConfig.setProperty("org.eclipse.jdt.core.formatter.indentation.size", "4");
        // 行最大长度
        formatterConfig.setProperty("org.eclipse.jdt.core.formatter.lineSplit", "120");
        // 方法大括号紧跟行尾（如 public void test() {）
        formatterConfig.setProperty("org.eclipse.jdt.core.formatter.brace_position_for_method", "end_of_line");
        // 字段间允许无空行（适配 MBG 生成的 POJO）
        formatterConfig.setProperty("org.eclipse.jdt.core.formatter.blank_lines_between_fields", "0");
        // 方法间空 1 行
        formatterConfig.setProperty("org.eclipse.jdt.core.formatter.blank_lines_between_methods", "1");
        return formatterConfig;
    }
}
