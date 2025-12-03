package org.codecrafterslab.unity.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ResultHolder {

    /**
     * 私有构造器，禁止实例化
     */
    private ResultHolder() {
    }

    private static final ThreadLocal<Data> data = new ThreadLocal<>();

    /**
     * 设置数据总数
     *
     * @param total 数据总数
     */
    public static void setTotal(Integer total) {
        Data hodlerData = getData();
        if (hodlerData == null) {
            hodlerData = Data.builder().total(total).build();
        } else {
            hodlerData.setTotal(total);
        }
        data.set(hodlerData);
    }

    public static Data getData() {
        return data.get();
    }

    public static <S> void setSummary(S summary) {
        Data hodlerData = getData();
        if (hodlerData == null) {
            hodlerData = Data.builder().summary(summary).build();
        } else {
            hodlerData.setSummary(summary);
        }
        data.set(hodlerData);
    }

    /**
     * 清除数据（必须调用，避免内存泄漏）
     */
    public static void clear() {
        data.remove();
    }

    @Setter
    @Getter
    @Builder
    public static class Data {
        private Integer total;
        private Object summary;
    }

}
