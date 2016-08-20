package com.gosenk.sports.alarm.api.dso;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id == null){
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
    }

}
