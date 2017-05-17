package com.gosenk.sports.alarm.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team extends BaseEntity{

    public Team(){
        super();
    }

    @Column(name = "identifier")
    private String identifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(name = "city")
    private String city;

    @Column(name = "mascot")
    private String mascot;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    /*@OneToMany(fetch = FetchType.EAGER, mappedBy = "team")
    //@JoinColumn(name = "id", referencedColumnName = "team_id")
    @OrderBy("time")
    private Set<Game> schedule = new HashSet<>(0);*/




    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonIgnore
    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMascot() {
        return mascot;
    }

    public void setMascot(String mascot) {
        this.mascot = mascot;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    /*public Set<Game> getSchedule() {
        return schedule;
    }

    public void setSchedule(Set<Game> schedule) {
        this.schedule = schedule;
    }*/
}
