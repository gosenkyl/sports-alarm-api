package com.gosenk.sports.alarm.api.dso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game")
public class Game extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_team_id", nullable = false)
    private Team opponentTeam;

    @Column(name="time")
    private Date time;

    @Column(name="home_flag")
    private boolean homeFlag;

    @JsonIgnore
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @JsonIgnore
    public Team getOpponentTeam() {
        return opponentTeam;
    }

    public String getOpponentTeamId(){
        return opponentTeam.getId();
    }

    public void setOpponentTeam(Team opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isHomeFlag() {
        return homeFlag;
    }

    public void setHomeFlag(boolean homeFlag) {
        this.homeFlag = homeFlag;
    }
}
