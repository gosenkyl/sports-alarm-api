package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.common.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("teams")
@CrossOrigin
public class TeamResource {

    @Autowired
    private TeamService teamService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Team> getAll(@RequestParam String leagueId){
        if(!StringUtils.isEmpty(leagueId)){
            return teamService.findByLeague(leagueId);
        }

        return teamService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Team findById(@PathVariable String id){
        if (id == null) {
            return null;
        } else {
            return teamService.findOne(id);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Team update(@RequestBody Team team){
        return teamService.save(team);
    }

}
