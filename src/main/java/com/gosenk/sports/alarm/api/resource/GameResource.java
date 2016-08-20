package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.api.dso.Game;
import com.gosenk.sports.alarm.api.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/games")
@CrossOrigin
public class GameResource {

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Game> getAll(){
        return (List) gameRepository.findAll();
    }

}
