package org.codecrafterslab.unity.response.api;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/07 09:03
 */
public interface IPageResult<T> extends IResult<T> {

    /**
     * 总数据数
     *
     * @return int
     */
    Integer getTotal();
}
