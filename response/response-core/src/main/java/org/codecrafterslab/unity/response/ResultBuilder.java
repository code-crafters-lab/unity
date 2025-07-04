package org.codecrafterslab.unity.response;

import org.codecrafterslab.unity.exception.api.Status;
import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.springframework.web.util.NestedServletException;

import java.io.Serializable;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/07 09:23
 */
public class ResultBuilder<D> implements Serializable {
    /**
     * 是否正常响应
     */
    private boolean success;

    /**
     * 响应编码
     */
    private Long code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private D data;

    /**
     * 分页总数据数
     */
    private Integer total;


    /**
     * 响应编码
     *
     * @param code 业务错误编码
     * @return ResultBuilder
     */
    public ResultBuilder<D> code(Long code) {
        this.code = code;
        return this;
    }

    /**
     * 提示信息
     *
     * @param message 信息
     * @return ResultBuilder
     */
    public ResultBuilder<D> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 返回数据
     *
     * @param data 数据
     * @return ResultBuilder
     */
    public ResultBuilder<D> data(D data) {
        this.data = data;
        return this;
    }

    /**
     * 返回数据
     *
     * @param total 总数据数
     * @return ResultBuilder
     */
    public ResultBuilder<D> total(Integer total) {
        this.total = total;
        return this;
    }


    /**
     * 成功响应
     *
     * @param message 响应信息
     * @param data    数据
     * @return Builder
     */
    public ResultBuilder<D> success(String message, D data, Integer total) {
        this.success = true;
        this.code = 0L;
        this.message = message;
        this.data = data;
        this.total = total;
        return this;
    }

    /**
     * 成功响应
     *
     * @param message 响应信息
     * @param data    数据
     * @return Builder
     */
    public ResultBuilder<D> success(String message, D data) {
        return success(message, data, null);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return Builder
     */
    public ResultBuilder<D> success(D data, Integer total) {
        return success(null, data, total);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return Builder
     */
    public ResultBuilder<D> success(D data) {
        return success("ok", data);
    }

    /**
     * 成功响应
     *
     * @return Builder
     */
    public ResultBuilder<D> success() {
        return success(null);
    }


    /**
     * 失败结果
     *
     * @param code    响应编码
     * @param message 响应信息
     * @param data    响应数据
     * @return Builder
     */
    public ResultBuilder<D> failure(long code, String message, D data) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }

    /**
     * 失败结果
     *
     * @param bizStatus 业务状态
     * @param data      响应数据
     * @return Builder
     */
    public ResultBuilder<D> failure(Status bizStatus, D data) {
        return failure(bizStatus.getCode(), bizStatus.getMessage(), data);
    }

    /**
     * 失败结果
     *
     * @param bizStatus 业务状态
     * @return Builder
     */
    public ResultBuilder<D> failure(Status bizStatus) {
        return failure(bizStatus, null);
    }

    /**
     * 异常结果
     *
     * @param exception Exception
     * @param data      响应数据
     * @return Builder
     */
    public ResultBuilder<D> failure(Exception exception, D data) {
        this.success = false;
        if (exception instanceof NestedServletException) {
            Throwable cause = exception.getCause();
            if (cause instanceof BizException) {
                BizException bizException = (BizException) cause;
                this.code = (long) bizException.getCode();
                this.message = bizException.getMessage();
            }
        } else if (exception instanceof BizException) {
            BizException bizException = (BizException) exception;
            this.code = (long) bizException.getCode();
            this.message = bizException.getMessage();
        } else {
            this.code = (long) BizStatus.INTERNAL_SERVER_ERROR.getCode();
            this.message = exception.getMessage();
        }
        this.data = data;
        return this;
    }


    /**
     * 异常结果
     *
     * @param exception Exception
     * @return Builder
     */
    public ResultBuilder<D> failure(Exception exception) {
        return failure(exception, null);
    }

    /**
     * 创建统一响应结果
     *
     * @return Result
     */
    public Result<D> build() {
        Result<D> result = new Result<>();
        result.success(success).code(code).message(message).data(data).total(total);
        return result;
    }

}
