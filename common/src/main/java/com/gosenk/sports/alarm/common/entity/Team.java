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

    @Column(name = "origin_city")
    private String originCity;

    @Column(name = "origin_mascot")
    private String originMascot;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "is_new")
    private boolean isNew;

    @Column(name = "image")
    private String image;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "homeTeam")
    @OrderBy("dateTime")
    private Set<Game> homeGames = new HashSet<>(0);

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "awayTeam")
    @OrderBy("dateTime")
    private Set<Game> awayGames = new HashSet<>(0);

    @Transient
    private String leagueId;

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

    public String getLeagueId(){
        if(this.league != null){
            return this.league.getId();
        }
        return this.leagueId;
    }

    public void setLeagueId(String leagueId) { this.leagueId = leagueId; }

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

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginMascot() {
        return originMascot;
    }

    public void setOriginMascot(String originMascot) {
        this.originMascot = originMascot;
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

    public boolean getIsNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Game> getHomeGames() {
        return homeGames;
    }

    public void setHomeGames(Set<Game> homeGames) {
        this.homeGames = homeGames;
    }

    public Set<Game> getAwayGames() {
        return awayGames;
    }

    public void setAwayGames(Set<Game> awayGames) {
        this.awayGames = awayGames;
    }
}
