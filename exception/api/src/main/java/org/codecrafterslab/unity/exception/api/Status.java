package org.codecrafterslab.unity.exception.api;

/**
 * 统一状态接口
 */
public interface Status {

    /**
     * 状态编码
     *
     * @return Long
     */
    int getCode();

    /**
     * 状态信息
     *
     * @return String
     */
    String getMessage();

    /**
     * 重置状态信息
     * 主要用于在响应成功时，定制成功信息
     *
     * @param message 信息内容
     * @return UnityStatus
     */
    default Status message(String message) {
        return this;
    }

    /**
     * Http 状态码值
     *
     * @return int
     */
    int getHttpStatus();
}
