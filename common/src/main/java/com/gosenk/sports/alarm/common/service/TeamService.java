package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.common.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService extends BaseService<Team, TeamRepository>{

    @Autowired
    public TeamService(TeamRepository repository){
        super(repository);
    }

    public List<Team> findByLeague(String leagueId){
        return getRepository().findByLeague(leagueId);
    }
}
