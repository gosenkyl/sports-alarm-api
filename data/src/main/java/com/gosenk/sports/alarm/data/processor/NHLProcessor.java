package com.gosenk.sports.alarm.data.processor;

import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NHLProcessor extends MLBStatsAPIProcessor implements Processor {

    private final static String filePrefix = "nhl";
    private final static String leagueId = "5150";
    private final static String season = "2018";
    private final static String seasonStartDate = "2017-08-01";
    private final static String seasonEndDate = "2018-07-31";

    public NHLProcessor(){
        super(filePrefix, leagueId, season, seasonStartDate, seasonEndDate);
    }

    public void process(boolean processAsSQL){
        try {
            Map<String, TeamLight> teamMap = processTeams(processAsSQL);
            processGames(processAsSQL, teamMap);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
