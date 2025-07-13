package org.codecrafterslab.unity.response;

import lombok.Data;
import org.codecrafterslab.unity.exception.api.Status;
import org.codecrafterslab.unity.response.api.IPageResult;

/**
 * Rest 接口响应结果
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2018/11/04 23:07
 */
@Data
@SuppressWarnings("unchecked")
public class Result<T, S> implements IPageResult<T, S> {

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
     * 汇总数据
     */
    private S summary;

    /**
     * 分页返回数据时的总数据条数
     */
    private Integer total;


    private static <D, S> ResultBuilder<D, S> builder() {
        return new ResultBuilder<>();
    }

    public static <D, S> Result<D, S> success() {
        return (Result<D, S>) builder().success().build();
    }

    public static <D, S> Result<D, S> success(D data) {
        return (Result<D, S>) builder().success(data).build();
    }

    public static <D, S> Result<D, S> success(D data, S summary) {
        return (Result<D, S>) builder().success(data).summary(summary).build();
    }

    public static <D, S> Result<D, S> success(D data, int total) {
        return (Result<D, S>) builder().success(data, total).build();
    }

    public static <D, S> Result<D, S> success(D data, int total, S summary) {
        return (Result<D, S>) builder().success(data, total).summary(summary).build();
    }

    public static <D, S> Result<D, S> success(String message, D data) {
        return (Result<D, S>) builder().success(message, data).build();
    }

    public static <D, S> Result<D, S> success(String message, D data, S summary) {
        return (Result<D, S>) builder().success(message, data).summary(summary).build();
    }

    public static <D, S> Result<D, S> success(String message, D data, int total) {
        return (Result<D, S>) builder().success(message, data, total).build();
    }

    public static <D, S> Result<D, S> success(String message, D data, int total, S summary) {
        return (Result<D, S>) builder().success(message, data, total).summary(summary).build();
    }

    public static <D, S> Result<D, S> failure(long code, String message, D data) {
        return (Result<D, S>) builder().failure(code, message, data).build();
    }

    public static <D, S> Result<D, S> failure(long code, String message) {
        return (Result<D, S>) builder().failure(code, message, null).build();
    }

    public static <D, S> Result<D, S> failure(Exception exception, D data) {
        return (Result<D, S>) builder().failure(exception, data).build();
    }

    public static <D, S> Result<D, S> failure(Exception exception) {
        return (Result<D, S>) builder().failure(exception).build();
    }

    public static <D, S> Result<D, S> failure(Status bizStatus, D data) {
        return (Result<D, S>) builder().failure(bizStatus, data).build();
    }

    public static <D, S> Result<D, S> failure(Status bizStatus) {
        return (Result<D, S>) builder().failure(bizStatus).build();
    }

}
