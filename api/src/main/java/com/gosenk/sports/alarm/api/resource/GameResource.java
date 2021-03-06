package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("games")
@CrossOrigin
public class GameResource {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Game> getAll(){
        return gameService.findAll();
    }

}
