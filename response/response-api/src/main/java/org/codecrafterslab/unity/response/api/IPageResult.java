package org.codecrafterslab.unity.response.api;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/07 09:03
 */
public interface IPageResult<T, S> extends ISummaryResult<T, S> {

    /**
     * 总数据
     *
     * @return int
     */
    Integer getTotal();
}
