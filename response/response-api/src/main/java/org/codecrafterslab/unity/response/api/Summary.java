package org.codecrafterslab.unity.response.api;

public interface Summary<S> {

    /**
     * 汇总数据
     *
     * @return S
     */
    S getSummary();

    /**
     * 设置汇总数据
     *
     * @param summary S
     */
    void setSummary(S summary);
}
