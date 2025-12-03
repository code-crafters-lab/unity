package org.codecrafterslab.unity.response.api;

public interface SummaryResult<D, S> extends Summary<S>, Result<D> {

    /**
     * 汇总数据
     *
     * @return S
     */
    S getSummary();

}
