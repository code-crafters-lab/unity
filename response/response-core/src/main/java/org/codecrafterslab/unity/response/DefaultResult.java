package org.codecrafterslab.unity.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.response.api.Result;

/**
 * Rest 接口响应结果
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2018/11/04 23:07
 */
@Data
@EqualsAndHashCode
public class DefaultResult<T> implements Result<T> {

    /**
     * 是否成功
     */
    protected boolean success = false;

    /**
     * 响应态码
     */
    protected Long code;

    /**
     * 响应信息
     */
    protected String message;
    /**
     * 数据
     */
    protected T data;

    public DefaultResult() {
    }

    public DefaultResult(boolean success, Long code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
