package org.codecrafterslab.unity.response.api;

import java.io.Serializable;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/08/07 08:49
 */
public interface Result<T> extends Serializable {

    /**
     * 是否正常完成业务逻辑
     *
     * @return 成功返回 true ,否则返回 false
     */
    boolean isSuccess();

    /**
     * 响应编码
     *
     * @return 异常响应时为具体的错误编码，正常响应可为空或者 0
     */
    Long getCode();

    /**
     * 响应信息
     *
     * @return 响应成功或失败时需要提示的信息
     */
    String getMessage();

    /**
     * 响应数据对象
     *
     * @return T
     */
    T getData();

}
