package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.repository.GameRepository;
import com.gosenk.sports.alarm.common.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService extends BaseService<Game, GameRepository>{

    @Autowired
    public GameService(GameRepository repository){
        super(repository);
    }

}
