package org.codecrafterslab.unity.response.api;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2025/07/22 21:49
 */
public interface PageResult<T> extends Result<T> {

    /**
     * 总数据
     *
     * @return Integer
     */
    Integer getTotal();

//    /**
//     * 每页数据大写
//     *
//     * @return Integer
//     */
//    default Integer getPageSize() {
//        return null;
//    }
//
//    /**
//     * 当前页码
//     *
//     * @return Integer
//     */
//    default Integer getCurrentPage() {
//        return null;
//    }
//
//    /**
//     * 总页数
//     *
//     * @return Integer
//     */
//    default Integer getPages() {
//        if (this.getPageSize() == 0) {
//            return 0;
//        } else if (this.getTotal() != null && this.getPageSize() != null) {
//            int pages = this.getTotal() / this.getPageSize();
//            if (this.getTotal() % this.getPageSize() != 0) {
//                ++pages;
//            }
//            return pages;
//        }
//        return null;
//    }

}
