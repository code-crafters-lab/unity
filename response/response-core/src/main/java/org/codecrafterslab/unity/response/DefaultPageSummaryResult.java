package org.codecrafterslab.unity.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.response.api.PageSummaryResult;

@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPageSummaryResult<D, S> extends DefaultPageResult<D> implements PageSummaryResult<D, S> {

    /**
     * 分页返回数据时的总数据条数
     */
    protected S summary;

    public DefaultPageSummaryResult() {
        super();
    }

    public DefaultPageSummaryResult(boolean success, Long code, String message, D data, Integer total) {
        super(success, code, message, data, total);
    }

}
