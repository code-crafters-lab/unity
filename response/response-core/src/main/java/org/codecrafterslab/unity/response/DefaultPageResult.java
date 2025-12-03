package org.codecrafterslab.unity.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.response.api.PageResult;

@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPageResult<D> extends DefaultResult<D> implements PageResult<D> {

    /**
     * 分页返回数据时的总数据条数
     */
    protected Integer total;

    public DefaultPageResult() {
        super();
    }

    public DefaultPageResult(boolean success, Long code, String message, D data, Integer total) {
        super(success, code, message, data);
        this.total = total;
    }

}
