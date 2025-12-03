package org.codecrafterslab.unity.response.api;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2025/07/22 21:49
 */
public interface PageResult<T> extends Page, Result<T> {

    /**
     * 总数据
     *
     * @return Integer
     */
    Integer getTotal();

}
