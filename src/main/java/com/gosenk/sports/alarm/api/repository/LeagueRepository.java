package com.gosenk.sports.alarm.api.repository;

import com.gosenk.sports.alarm.api.dso.League;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends CrudRepository<League, String>{

}
