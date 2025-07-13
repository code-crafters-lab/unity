package org.codecrafterslab.unity.response.api;

public interface ISummaryResult<T, S> extends IResult<T> {

    /**
     * 汇总数据
     *
     * @return S
     */
    S getSummary();

}
