package org.codecrafterslab.unity.response;

import org.codecrafterslab.unity.exception.api.Status;
import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.codecrafterslab.unity.response.api.PageSummaryResult;
import org.springframework.lang.Nullable;
import org.springframework.web.util.NestedServletException;

import java.io.Serializable;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/07 09:23
 */
class ResultBuilder<D, S> implements Serializable {
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
     * 汇总数据
     */
    private S summary;

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
    public ResultBuilder<D, S> code(Long code) {
        this.code = code;
        return this;
    }

    /**
     * 提示信息
     *
     * @param message 信息
     * @return ResultBuilder
     */
    public ResultBuilder<D, S> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 返回数据
     *
     * @param data 数据
     * @return ResultBuilder
     */
    public ResultBuilder<D, S> data(D data) {
        this.data = data;
        return this;
    }

    /**
     * 汇总数据
     *
     * @param summary 数据
     * @return ResultBuilder
     */
    public ResultBuilder<D, S> summary(S summary) {
        this.summary = summary;
        return this;
    }

    /**
     * 返回数据
     *
     * @param total 总数据数
     * @return ResultBuilder
     */
    public ResultBuilder<D, S> total(Integer total) {
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
    public ResultBuilder<D, S> success(@Nullable String message, @Nullable D data,
                                       @Nullable Integer total, @Nullable S summary) {
        this.success = true;
        this.code = 0L;
        this.message = message;
        this.data = data;
        this.total = total;
        this.summary = summary;
        return this;
    }

    /**
     * 成功响应
     *
     * @param message 响应信息
     * @param data    数据
     * @return Builder
     */
    public ResultBuilder<D, S> success(@Nullable String message, @Nullable D data, @Nullable Integer total) {
        return success(message, data, total, null);
    }

    /**
     * 成功响应
     *
     * @param message 响应信息
     * @param data    数据
     * @return Builder
     */
    public ResultBuilder<D, S> success(@Nullable String message, @Nullable D data) {
        return success(message, data, null);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return Builder
     */
    public ResultBuilder<D, S> success(D data, Integer total) {
        return success(null, data, total);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return Builder
     */
    public ResultBuilder<D, S> success(@Nullable D data) {
        return success(BizStatus.OK.getMessage().toLowerCase(), data);
    }

    /**
     * 成功响应
     *
     * @return Builder
     */
    public ResultBuilder<D, S> success() {
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
    public ResultBuilder<D, S> failure(long code, String message, @Nullable D data) {
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
    public ResultBuilder<D, S> failure(Status bizStatus, @Nullable D data) {
        return failure(bizStatus.getCode(), bizStatus.getMessage(), data);
    }

    /**
     * 失败结果
     *
     * @param bizStatus 业务状态
     * @return Builder
     */
    public ResultBuilder<D, S> failure(Status bizStatus) {
        return failure(bizStatus, null);
    }

    /**
     * 异常结果
     *
     * @param exception Exception
     * @param data      响应数据
     * @return Builder
     */
    public ResultBuilder<D, S> failure(Exception exception, @Nullable D data) {
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
    public ResultBuilder<D, S> failure(Exception exception) {
        return failure(exception, null);
    }

    /**
     * 创建统一响应结果
     *
     * @return Result
     */
    public PageSummaryResult<D, S> build() {
        DefaultPageSummaryResult<D, S> result = new DefaultPageSummaryResult<>();
        result.setSuccess(success);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        result.setSummary(summary);
        result.setTotal(total);
        return result;
    }

}
