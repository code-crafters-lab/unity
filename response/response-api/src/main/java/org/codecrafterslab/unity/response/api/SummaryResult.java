package org.codecrafterslab.unity.response.api;

public interface SummaryResult<D, S> extends Result<D> {

    /**
     * 汇总数据
     *
     * @return S
     */
    S getSummary();
    
}
