package org.codecrafterslab.unity.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.response.api.PageSummaryResult;

@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPageResult<D, S> extends DefaultSummaryResult<D, S> implements PageSummaryResult<D, S> {

    /**
     * 分页返回数据时的总数据条数
     */
    protected Integer total;

    public DefaultPageResult() {
        super();
    }

    public DefaultPageResult(boolean success, Long code, String message, D data, Integer total, S summary) {
        super(success, code, message, data, summary);
        this.total = total;
    }

}
