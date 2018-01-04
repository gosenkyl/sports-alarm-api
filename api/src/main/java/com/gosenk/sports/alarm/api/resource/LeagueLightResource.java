package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.commonlight.entity.LeagueLight;
import com.gosenk.sports.alarm.commonlight.service.LeagueLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("light/leagues")
@CrossOrigin
public class LeagueLightResource {

    @Autowired
    private LeagueLightService leagueLightService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LeagueLight> getAll(){
        return leagueLightService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public LeagueLight findById(@PathVariable String id){
        if (id == null) {
            return null;
        } else {
            return leagueLightService.findOne(id);
        }
    }

}
