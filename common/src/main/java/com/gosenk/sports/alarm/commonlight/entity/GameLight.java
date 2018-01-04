package com.gosenk.sports.alarm.commonlight.entity;

import com.gosenk.sports.alarm.common.entity.BaseAuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "game")
public class GameLight extends BaseAuditEntity {

    public GameLight(){
        super();
    }

    @Column(name="identifier")
    private String identifier;

    @Column(name = "league_id")
    private String leagueId;

    @Column(name = "home_team_id")
    private String homeTeamId;

    @Column(name = "away_team_id")
    private String awayTeamId;

    @Column(name="date_time")
    private Long dateTime;

    @Column(name="deleted")
    private boolean deleted;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
