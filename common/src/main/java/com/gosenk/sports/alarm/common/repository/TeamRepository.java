package com.gosenk.sports.alarm.common.repository;

import com.gosenk.sports.alarm.common.entity.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, String>{

    @Query("SELECT t FROM Team t WHERE t.league.id = ?1 AND t.city = ?2 AND t.mascot = ?3")
    List<Team> findByLeagueCityMascot(String leagueId, String city, String mascot);

    @Query("SELECT t FROM Team t WHERE t.league.id = ?1 AND t.identifier = ?2")
    List<Team> findByLeagueIdentifier(String leagueId, String identifier);
}
