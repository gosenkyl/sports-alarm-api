package com.gosenk.sports.alarm.commonlight.service;

import com.gosenk.sports.alarm.common.service.BaseServiceImpl;
import com.gosenk.sports.alarm.commonlight.entity.LeagueLight;
import com.gosenk.sports.alarm.commonlight.repository.LeagueLightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueLightService extends BaseServiceImpl<LeagueLight, LeagueLightRepository> {

    @Autowired
    public LeagueLightService(LeagueLightRepository repository){
        super(repository);
    }

}
