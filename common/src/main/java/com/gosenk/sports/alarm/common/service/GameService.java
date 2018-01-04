package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameService extends BaseServiceImpl<Game, GameRepository> {

    @Autowired
    public GameService(GameRepository repository){
        super(repository);
    }

}
