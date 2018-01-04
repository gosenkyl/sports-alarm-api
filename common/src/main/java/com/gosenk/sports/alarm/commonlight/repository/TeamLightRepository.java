package com.gosenk.sports.alarm.commonlight.repository;


import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamLightRepository extends CrudRepository<TeamLight, String>{

    @Query("SELECT t FROM TeamLight t WHERE t.leagueId = ?1 ORDER BY t.city ASC, t.mascot ASC")
    List<TeamLight> findByLeague(String leagueId);

    @Query("SELECT t FROM TeamLight t WHERE t.leagueId = ?1 AND t.city = ?2 AND t.mascot = ?3 ORDER BY t.city ASC, t.mascot ASC")
    List<TeamLight> findByLeagueCityMascot(String leagueId, String city, String mascot);

    @Query("SELECT t FROM TeamLight t WHERE t.leagueId = ?1 AND t.identifier = ?2 ORDER BY t.city ASC, t.mascot ASC")
    List<TeamLight> findByLeagueIdentifier(String leagueId, String identifier);

}
