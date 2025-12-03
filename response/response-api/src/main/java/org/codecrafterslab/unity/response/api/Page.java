package org.codecrafterslab.unity.response.api;

public interface Page {
    /**
     * 总数据
     *
     * @return Integer
     */
    Integer getTotal();

    /**
     * 设置分页总数据量
     *
     * @param total Integer
     */
    void setTotal(Integer total);

}
