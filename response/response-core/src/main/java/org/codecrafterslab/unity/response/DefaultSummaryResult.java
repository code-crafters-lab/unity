package org.codecrafterslab.unity.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codecrafterslab.unity.response.api.SummaryResult;

@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultSummaryResult<D, S> extends DefaultResult<D> implements SummaryResult<D, S> {

    /**
     * 汇总数据
     */
    protected S summary;

    public DefaultSummaryResult() {
        super();
    }

    public DefaultSummaryResult(boolean success, Long code, String message, D data, S summary) {
        super(success, code, message, data);
        this.summary = summary;
    }

}
