package com.gosenk.sports.alarm.common.repository;

import com.gosenk.sports.alarm.common.entity.Team;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, String>{

    @Query("SELECT t FROM Team t WHERE t.league.id = ?1")
    List<Team> findByLeague(String leagueId);

    @Query("SELECT t FROM Team t WHERE t.league.id = ?1 AND t.city = ?2 AND t.mascot = ?3")
    List<Team> findByLeagueCityMascot(String leagueId, String city, String mascot);

    @Query("SELECT t FROM Team t WHERE t.league.id = ?1 AND t.identifier = ?2")
    List<Team> findByLeagueIdentifier(String leagueId, String identifier);

}
