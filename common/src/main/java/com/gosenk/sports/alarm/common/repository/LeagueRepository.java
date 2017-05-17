package com.gosenk.sports.alarm.common.repository;

import com.gosenk.sports.alarm.common.entity.League;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends CrudRepository<League, String>{

}
