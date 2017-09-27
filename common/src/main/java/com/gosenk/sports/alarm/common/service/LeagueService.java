package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueService extends BaseService<League, LeagueRepository>{

    @Autowired
    public LeagueService(LeagueRepository repository){
        super(repository);
    }

}
