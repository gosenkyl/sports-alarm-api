package com.gosenk.sports.alarm.commonlight.entity;

import com.gosenk.sports.alarm.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "league")
public class LeagueLight extends BaseEntity {

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "description")
    private String description;


    public int getSequence() {
        return sequence;
    }

    public String getDescription() {
        return description;
    }
}
