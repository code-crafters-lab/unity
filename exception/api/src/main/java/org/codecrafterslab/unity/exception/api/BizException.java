package org.codecrafterslab.unity.exception.api;

import org.codecrafterslab.unity.exception.api.impl.DefaultStatus;

public class BizException extends RuntimeException {

    protected Status status;

    public Status getStatus() {
        return status;
    }

    /**
     * @param status     统一状态
     * @param formatArgs 信息格式化参数
     */
    public BizException(Status status, Object... formatArgs) {
        super(String.format(status.getMessage(), formatArgs));
        this.status = status;
    }

    /**
     * @param code       错误编码
     * @param message    错误信息
     * @param httpStatus HTTP 状态码
     * @param formatArgs 信息格式化参数
     */
    public BizException(int code, String message, int httpStatus, Object... formatArgs) {
        this(new DefaultStatus(code, message, httpStatus), formatArgs);
    }
}
