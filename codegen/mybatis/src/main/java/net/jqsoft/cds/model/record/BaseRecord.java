package net.jqsoft.cds.model.record;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseRecord {

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 更新人
     */
    private String updatedBy;
}
