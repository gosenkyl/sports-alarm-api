package com.gosenk.sports.alarm.common.repository;

import com.gosenk.sports.alarm.common.entity.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, String>{

    @Query("SELECT g FROM Game g WHERE g.league.id = ?1 AND g.identifier = ?2")
    List<Game> findByLeagueIdentifier(String leagueId, String identifier);

}
