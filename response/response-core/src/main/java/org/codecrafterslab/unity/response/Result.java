package org.codecrafterslab.unity.response;

import org.codecrafterslab.unity.exception.api.Status;
import org.codecrafterslab.unity.response.api.IPageResult;

/**
 * Rest 接口响应结果
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2018/11/04 23:07
 */
@SuppressWarnings("unchecked")
public class Result<T> implements IPageResult<T> {

    /**
     * 是否成功
     */
    private boolean success = false;

    /**
     * 响应态码
     */
    private Long code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 分页返回数据时的总数据条数
     */
    private Integer total;

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public Integer getTotal() {
        return total;
    }

    public Result<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public Result<T> code(long code) {
        this.code = code;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> total(Integer total) {
        this.total = total;
        return this;
    }


    private static <D> ResultBuilder<D> builder() {
        return new ResultBuilder<>();
    }

    public static <D> Result<D> success() {
        return (Result<D>) builder().success().build();
    }

    public static <D> Result<D> success(D data) {
        return (Result<D>) builder().success(data).build();
    }

    public static <D> Result<D> success(D data, int total) {
        return (Result<D>) builder().success(data, total).build();
    }

    public static <D> Result<D> success(String message, D data) {
        return (Result<D>) builder().success(message, data).build();
    }

    public static <D> Result<D> success(String message, D data, int total) {
        return (Result<D>) builder().success(message, data, total).build();
    }

    public static <D> Result<D> failure(long code, String message, D data) {
        return (Result<D>) builder().failure(code, message, data).build();
    }

    public static <D> Result<D> failure(long code, String message) {
        return (Result<D>) builder().failure(code, message, null).build();
    }

    public static <D> Result<D> failure(Exception exception, D data) {
        return (Result<D>) builder().failure(exception, data).build();
    }

    public static <D> Result<D> failure(Exception exception) {
        return (Result<D>) builder().failure(exception).build();
    }

    public static <D> Result<D> failure(Status bizStatus, D data) {
        return (Result<D>) builder().failure(bizStatus, data).build();
    }

    public static <D> Result<D> failure(Status bizStatus) {
        return (Result<D>) builder().failure(bizStatus).build();
    }

}
