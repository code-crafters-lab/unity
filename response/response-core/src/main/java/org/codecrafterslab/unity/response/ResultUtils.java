package org.codecrafterslab.unity.response;

import org.codecrafterslab.unity.exception.api.Status;
import org.codecrafterslab.unity.response.api.PageResult;
import org.codecrafterslab.unity.response.api.PageSummaryResult;
import org.codecrafterslab.unity.response.api.Result;
import org.codecrafterslab.unity.response.api.SummaryResult;

@SuppressWarnings("unchecked")
public class ResultUtils {

    private static <D, S> ResultBuilder<D, S> builder() {
        return new ResultBuilder<>();
    }

    public static Result<?> success() {
        return builder().success().build();
    }

    public static <D> Result<D> success(D data) {
        return (Result<D>) builder().success(data).build();
    }

    public static <D> Result<D> success(String message, D data) {
        return (Result<D>) builder().success(message, data).build();
    }

    public static <D, S> SummaryResult<D, S> success(D data, S summary) {
        return (SummaryResult<D, S>) builder().success(data).summary(summary).build();
    }

    public static <D, S> SummaryResult<D, S> success(String message, D data, S summary) {
        return (SummaryResult<D, S>) builder().success(message, data).summary(summary).build();
    }

    public static <D> PageResult<D> success(D data, Integer total) {
        return (PageResult<D>) builder().success(data, total).build();
    }

    public static <D> PageResult<D> success(String message, D data, Integer total) {
        return (PageResult<D>) builder().success(message, data, total).build();
    }

    public static <D, S> PageSummaryResult<D, S> success(D data, Integer total, S summary) {
        return (PageSummaryResult<D, S>) builder().success(data, total).summary(summary).build();
    }

    public static <D, S> PageSummaryResult<D, S> success(String message, D data, Integer total, S summary) {
        return (PageSummaryResult<D, S>) builder().success(message, data, total).summary(summary).build();
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
