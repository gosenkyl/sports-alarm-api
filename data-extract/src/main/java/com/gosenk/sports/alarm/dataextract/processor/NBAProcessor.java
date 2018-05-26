package com.gosenk.sports.alarm.dataextract.processor;

import com.gosenk.sports.alarm.common.entity.DataReport;
import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.entity.Team;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://github.com/kshvmdn/nba.js/blob/master/docs/api/DATA.md

@Service
public class NBAProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "NBA";

    private static final String scheduleURL = "http://data.nba.net/data/10s/prod/v1/{year}/schedule.json";
    private static final String teamsURL = "http://data.nba.net/data/10s/prod/v1/{year}/teams.json";

    private League league = null;

    @PostConstruct
    public void setLeague(){
        this.league = getLeague(leagueId);
    }

    private Map<String, Team> teamMap = new HashMap<>();

    public DataReport process(boolean processAsSQL) {

        if(league == null){
            throw new RuntimeException(String.format("LEAGUE %s NOT FOUND", leagueId));
        }

        DataReport result = new DataReport();
        result.setLeague(league);
        result.setDate(new Date());

        // TODO Should be command line/gradle param? or database league start/end date?
        String year = "2017";

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        try {
            // Teams
            String str = restTemplate.getForObject(teamsURL, String.class, year);

            JSONObject initial = new JSONObject(str);
            JSONObject league = initial.getJSONObject("league");
            JSONArray standard = league.getJSONArray("standard");

            for (Object teamObj : standard) {
                JSONObject tmp = (JSONObject) teamObj;
                if((boolean) tmp.get("isNBAFranchise")){
                    Team team = getOrCreateTeam(tmp);
                    teamMap.put(team.getIdentifier(), team);
                }
            }

            // Schedule
            str = restTemplate.getForObject(scheduleURL, String.class, year);

            initial = new JSONObject(str);
            league = initial.getJSONObject("league");
            standard = league.getJSONArray("standard");

            for (Object aGameList : standard) {
                JSONObject gameObj = (JSONObject) aGameList;
                if((Integer) gameObj.get("seasonStageId") == 2) {
                    result.incTotalCount();
                    createOrUpdateGame(result, gameObj);
                }
            }

        } catch(HttpClientErrorException e){
            System.out.println(String.format("CANT PROCESS ON %s", year));
        } catch(Exception e){
            System.out.println(String.format("CANT PROCESS ON %s", year));
            e.printStackTrace();
        }

        return result;
    }

    private Team getOrCreateTeam(JSONObject teamObj){
        String teamId = teamObj.get("teamId").toString();

        List<Team> teams = teamRepository.findByLeagueIdentifier(leagueId, teamId);

        if(teams != null && teams.size() > 0){
            if(teams.size() > 1){
                System.out.println(String.format("MORE THAN 1 TEAM FOUND FOR %s %s", leagueId, teamId));
            }

            // TODO - Is there a need to check for City/Mascot change or is a new team created?
            return teams.get(0);
        }

        String city = teamObj.get("city").toString();
        String mascot = teamObj.get("nickname").toString();

        System.out.println(String.format("CREATING NEW TEAM FOR %s %s", leagueId, teamId));

        Team team = new Team();

        team.setNew(true);
        team.setLeague(league);
        team.setIdentifier(teamId);

        // TODO - Remove these 2 lines, should be manual for NEW teams to fix things like Chi White Sox as city for Chicago - White Sox
        team.setCity(city);
        team.setMascot(mascot);

        team.setOriginCity(city);
        team.setOriginMascot(mascot);

        teamRepository.save(team);

        return team;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd h:mm a");

    private void createOrUpdateGame(DataReport result, JSONObject gameObj) throws ParseException {
        String gameId = gameObj.get("gameId").toString();

        JSONObject homeTeamObj = gameObj.getJSONObject("hTeam");
        String homeTeamId = homeTeamObj.get("teamId").toString();
        JSONObject awayTeamObj = gameObj.getJSONObject("vTeam");
        String awayTeamId = awayTeamObj.get("teamId").toString();

        String dateStr = gameObj.get("startDateEastern").toString(); //20180406
        String timeStr = gameObj.get("startTimeEastern").toString().replace("ET", "").trim(); //10:00 PM ET

        Date dateTime = sdf.parse(dateStr + " " + timeStr);
        long dateTimeMillis = dateTime.getTime();

        Game game = getGame(league, gameId);

        if(game != null){
            if(updateGame(game, dateTimeMillis)){
                result.incUpdateCount();
            }
        } else {
            Team homeTeam = teamMap.get(homeTeamId);
            Team awayTeam = teamMap.get(awayTeamId);

            game = createGame(league, gameId, homeTeam, awayTeam, dateTimeMillis);
            result.incInsertCount();
        }

        System.out.println(game.getHomeTeam().getOriginMascot() + " vs " + game.getAwayTeam().getOriginMascot());
    }

}
