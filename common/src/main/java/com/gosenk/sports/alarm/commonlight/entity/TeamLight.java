package com.gosenk.sports.alarm.commonlight.entity;

import com.gosenk.sports.alarm.common.entity.BaseAuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "team")
public class TeamLight extends BaseAuditEntity {

    public TeamLight(){
        super();
    }

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "league_id")
    private String leagueId;

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

    @Column(name = "deleted")
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

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
