package com.gosenk.sports.alarm.common.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "league")
public class League extends BaseEntity {
    @Column(name = "sequence")
    private int sequence;

    @Column(name = "description")
    private String description;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "league")
    @OrderBy("city, mascot")
    private Set<Team> teams = new HashSet<>(0);

    public int getSequence() {
        return sequence;
    }

    public String getDescription() {
        return description;
    }

    public Set<Team> getTeams() {
        return teams;
    }
}
