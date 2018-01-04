package com.gosenk.sports.alarm.commonlight.entity;

import com.gosenk.sports.alarm.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "league")
public class LeagueLight extends BaseEntity {

    @Column(name="parse_from_date")
    private Date parseFromDate;

    @Column(name="parse_to_date")
    private Date parseToDate;

    @Column(name = "sequence")
    private int sequence;

    public Date getParseFromDate() {
        return parseFromDate;
    }

    public void setParseFromDate(Date parseFromDate) {
        this.parseFromDate = parseFromDate;
    }

    public Date getParseToDate() {
        return parseToDate;
    }

    public void setParseToDate(Date parseToDate) {
        this.parseToDate = parseToDate;
    }

    public int getSequence() {
        return sequence;
    }

}
