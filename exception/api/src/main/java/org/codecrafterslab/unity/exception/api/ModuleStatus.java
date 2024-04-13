package org.codecrafterslab.unity.exception.api;

public interface ModuleStatus extends Status {

    @Override
    default int getCode() {
        int one = 0;
        if (!getModule().internal()) {
            int status = getHttpStatus();
            if (status >= 400 && status < 500) {
                one = 4;
            } else if (status >= 500) {
                one = 5;
            }
        }
        int two = getModule().getValue();
        if (two < 0 || two > 999) {
            String format = String.format("%s 超出模块编码范围 %s", two, "1~999");
            throw new RuntimeException(format);
        }
        int three = getMask();
        if (three < 0 || three > 99999) {
            throw new RuntimeException(String.format("%s 超出错误编码范围 %s", three, "1~99999"));
        }
        String code = String.format("%s%s%s", String.format("%01d", one), String.format("%03d", two), String.format("%05d", three));
        return Integer.parseInt(code);
    }

    /**
     * 获取模块信息
     *
     * @return Module
     */
    Module getModule();

    /**
     * 错误码唯一标识
     *
     * @return int
     */
    int getMask();
}
