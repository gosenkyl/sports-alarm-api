package com.gosenk.sports.alarm.commonlight.repository;


import com.gosenk.sports.alarm.commonlight.entity.GameLight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameLightRepository extends CrudRepository<GameLight, String>{

    @Query("SELECT g FROM GameLight g WHERE g.leagueId = ?1 AND g.identifier = ?2 ORDER BY g.dateTime ASC")
    List<GameLight> findByLeagueIdentifier(String leagueId, String identifier);

    @Query("SELECT g FROM GameLight g WHERE g.homeTeamId = ?1 OR g.awayTeamId = ?1 ORDER BY g.dateTime ASC")
    List<GameLight> findByTeamId(String teamId);

    @Query("SELECT g FROM GameLight g WHERE g.awayTeamId IN ?1 OR g.homeTeamId IN ?1 ORDER BY g.dateTime ASC")
    List<GameLight> findByTeamIds(List<String> teamIds);
}
