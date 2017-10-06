package com.gosenk.sports.alarm.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseAuditEntity extends BaseEntity {

    @Column(name = "created")
    private long created;

    @Column(name = "modified")
    private long modified;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }
}
