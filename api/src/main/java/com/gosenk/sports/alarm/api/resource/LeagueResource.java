package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("leagues")
@CrossOrigin
public class LeagueResource {

    @Autowired
    private LeagueService leagueService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<League> getAll(){
        return leagueService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public League findById(@PathVariable String id){
        if (id == null) {
            return null;
        } else {
            return leagueService.findOne(id);
        }
    }

}
