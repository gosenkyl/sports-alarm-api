package com.gosenk.sports.alarm.common.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "data_report")
public class DataReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(name="date")
    private Date date;

    @Column(name="update_count")
    private int updateCount;

    @Column(name="insert_count")
    private int insertCount;

    @Column(name="total_count")
    private int totalCount;

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(int insertCount) {
        this.insertCount = insertCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void incInsertCount(){
        this.insertCount++;
    }

    public void incUpdateCount(){
        this.updateCount++;
    }

    public void incTotalCount(){
        this.totalCount++;
    }
}
