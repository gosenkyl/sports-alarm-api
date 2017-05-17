package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/leagues")
@CrossOrigin
public class LeagueResource {

    @Autowired
    private LeagueRepository leagueRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<League> getAll(){
        return (List) leagueRepository.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public League findById(@PathVariable String id){
        if (id == null) {
            return null;
        } else {
            return leagueRepository.findOne(id);
        }
    }

}
