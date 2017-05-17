package com.gosenk.sports.alarm.common.object;

import java.util.Date;

public class ProcessorResult {

    public ProcessorResult(String league){
        this.runTime = new Date();
        this.league = league;
    }

    private String league;
    private Date runTime;
    private int updateCount;
    private int insertCount;

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
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

    public void incInsertCount(){
        this.insertCount++;
    }

    public void incUpdateCount(){
        this.updateCount++;
    }
}
