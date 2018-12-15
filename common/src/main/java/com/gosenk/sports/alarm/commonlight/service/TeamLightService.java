package com.gosenk.sports.alarm.commonlight.service;

import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import com.gosenk.sports.alarm.commonlight.repository.TeamLightRepository;
import com.gosenk.sports.alarm.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamLightService extends BaseServiceImpl<TeamLight, TeamLightRepository> {

    @Autowired
    public TeamLightService(TeamLightRepository repository){
        super(repository);
    }

    public List<TeamLight> findByLeague(String leagueId){
        return getRepository().findByLeague(leagueId);
    }

    public List<TeamLight> findByTeamIds(String teamIds){
        List<String> teamIdList = Arrays.asList(teamIds.split(","));

        if(teamIdList.isEmpty()){
            return new ArrayList<>();
        }

        return teamIdList.stream().map(id -> getRepository().findById(id).orElse(null)).collect(Collectors.toList());
    }
}
