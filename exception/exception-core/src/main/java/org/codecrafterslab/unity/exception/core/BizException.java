package org.codecrafterslab.unity.exception.core;


import org.codecrafterslab.unity.exception.api.Status;

/**
 * @author Wu Yujie
 * @since 0.1.0
 */
public final class BizException extends RuntimeException implements Status {
    private final int code;
    private final int httpStatus;

    public int getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * @param code       错误编码
     * @param message    错误信息
     * @param httpStatus HTTP 状态码
     * @param formatArgs 信息格式化参数
     */
    public BizException(int code, String message, int httpStatus, Object... formatArgs) {
        super(String.format(message, formatArgs));
        this.code = code;
        this.httpStatus = httpStatus;
    }

    /**
     * @param code       错误编码
     * @param message    错误信息
     * @param formatArgs 信息格式化参数
     */
    public BizException(int code, String message, Object... formatArgs) {
        this(code, message, 500, formatArgs);
    }

    /**
     * @param status     统一状态
     * @param formatArgs 信息格式化参数
     */
    public BizException(Status status, Object... formatArgs) {
        this(status.getCode(), status.getMessage(), status.getHttpStatus(), formatArgs);
    }
}
