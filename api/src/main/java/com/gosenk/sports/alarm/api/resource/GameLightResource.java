package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.commonlight.entity.GameLight;
import com.gosenk.sports.alarm.commonlight.service.GameLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("light/games")
@CrossOrigin
public class GameLightResource {

    @Autowired
    private GameLightService gameLightService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<GameLight> getAll(){
        return gameLightService.findAll();
    }

    @RequestMapping(value = "/team/{teamId}", method = RequestMethod.GET)
    public List<GameLight> getGamesByTeam(@PathVariable String teamId){
        return gameLightService.findByTeamId(teamId);
    }

    @RequestMapping(value = "teams", method = RequestMethod.GET)
    public List<GameLight> getByTeams(@RequestParam String ids){
        return gameLightService.findByTeamIds(ids);
    }

}
