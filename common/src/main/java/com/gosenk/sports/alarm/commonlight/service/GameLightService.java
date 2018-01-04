package com.gosenk.sports.alarm.commonlight.service;

import com.gosenk.sports.alarm.commonlight.entity.GameLight;
import com.gosenk.sports.alarm.commonlight.repository.GameLightRepository;
import com.gosenk.sports.alarm.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameLightService extends BaseServiceImpl<GameLight, GameLightRepository> {

    @Autowired
    public GameLightService(GameLightRepository repository){
        super(repository);
    }

    public List<GameLight> findByTeamId(String teamId){
        return getRepository().findByTeamId(teamId);
    }

    public List<GameLight> findByTeamIds(String teamIds){
        List<String> teamIdList = Arrays.asList(teamIds.split(","));

        if(teamIdList.isEmpty()){
            return new ArrayList<>();
        }

        //return teamIdList.stream().map(id -> getRepository().findOne(id)).collect(Collectors.toList());
        return getRepository().findByTeamIds(teamIdList);
    }

}
