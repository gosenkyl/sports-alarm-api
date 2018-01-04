package com.gosenk.sports.alarm.api.resource;

import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import com.gosenk.sports.alarm.commonlight.service.TeamLightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("light/teams")
@CrossOrigin
public class TeamLightResource {

    @Autowired
    private TeamLightService teamLightService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TeamLight> getAll(@RequestParam(required = false) String leagueId){
        if(!StringUtils.isEmpty(leagueId)){
            return teamLightService.findByLeague(leagueId);
        }

        return teamLightService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TeamLight findById(@PathVariable String id){
        if (id == null) {
            return null;
        } else {
            return teamLightService.findOne(id);
        }
    }

    @RequestMapping(value = "/ids", method = RequestMethod.GET)
    public List<TeamLight> getTeamsByIds(@RequestParam String ids){
        return teamLightService.findByTeamIds(ids);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public TeamLight update(@RequestBody TeamLight team){
        return teamLightService.save(team);
    }

}
