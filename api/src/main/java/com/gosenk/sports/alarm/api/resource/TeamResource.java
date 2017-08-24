package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.common.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/teams")
@CrossOrigin
public class TeamResource {

    @Autowired
    private TeamRepository teamRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Team> getAll(@RequestParam String leagueId){
        if(!StringUtils.isEmpty(leagueId)){
            return (List) teamRepository.findByLeague(leagueId);
        }

        return (List) teamRepository.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Team findById(@PathVariable String id){
        if (id == null) {
            return null;
        } else {
            return teamRepository.findOne(id);
        }
    }

}
