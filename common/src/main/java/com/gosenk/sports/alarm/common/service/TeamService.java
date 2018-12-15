package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.common.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService extends BaseServiceImpl<Team, TeamRepository> {

    @Autowired
    public TeamService(TeamRepository repository){
        super(repository);
    }

    public List<Team> findByLeague(String leagueId){
        return getRepository().findByLeague(leagueId);
    }

    public List<Team> findByTeamIds(String teamIds){
        List<String> teamIdList = Arrays.asList(teamIds.split(","));

        if(teamIdList.isEmpty()){
            return new ArrayList<>();
        }

        return teamIdList.stream().map(id -> getRepository().findById(id).orElse(null)).collect(Collectors.toList());
    }
}
