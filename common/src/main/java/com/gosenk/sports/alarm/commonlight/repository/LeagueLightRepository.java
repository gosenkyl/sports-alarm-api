package com.gosenk.sports.alarm.commonlight.repository;

import com.gosenk.sports.alarm.commonlight.entity.LeagueLight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueLightRepository extends CrudRepository<LeagueLight, String>{

}
