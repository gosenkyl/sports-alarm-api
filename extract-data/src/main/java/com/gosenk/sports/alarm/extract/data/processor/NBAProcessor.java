package com.gosenk.sports.alarm.extract.data.processor;

import com.gosenk.sports.alarm.extract.data.entity.Game;
import com.gosenk.sports.alarm.extract.data.entity.Team;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NBAProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "NBA";

    private final static String teamsURL = "http://data.nba.net/data/10s/prod/v1/{year}/teams.json";
    private final static String scheduleURL = "http://data.nba.net/data/10s/prod/v1/{year}/schedule.json";

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");

    public void process() {
        Map<String, Team> teamMap = new HashMap<>();
        Map<String, Team> identifierMap = new HashMap<>();

        // TODO Should be command line/gradle param? or database league start/end date?
        String year = "2018";

        try {
            FileOutputStream teamOutputStream = new FileOutputStream("mlb_teams.sql");
            FileOutputStream gameOutputStream = new FileOutputStream("mlb_games.sql");

            String use = "use " + schemaName + ";";
            teamOutputStream.write(use.getBytes());
            gameOutputStream.write(use.getBytes());

            teamOutputStream.write(baseTeamInsert.getBytes());
            gameOutputStream.write(baseGameInsert.getBytes());

            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            RestTemplate restTemplate = restTemplateBuilder.build();

            // Teams
            String str = restTemplate.getForObject(teamsURL, String.class, year);

            JSONObject initial = new JSONObject(str);
            JSONObject league = initial.getJSONObject("league");
            JSONArray standard = league.getJSONArray("standard");

            for (Object teamObj : standard) {
                JSONObject tmp = (JSONObject) teamObj;
                if ((boolean) tmp.get("isNBAFranchise")) {
                    createTeamInsertStatement(tmp, teamMap, identifierMap, teamOutputStream);
                }
            }

            // Schedule
            str = restTemplate.getForObject(scheduleURL, String.class, year);

            initial = new JSONObject(str);
            league = initial.getJSONObject("league");
            standard = league.getJSONArray("standard");

            for (Object aGameList : standard) {
                JSONObject gameObj = (JSONObject) aGameList;
                if ((Integer) gameObj.get("seasonStageId") == 2) {
                    createGameInsertStatement(gameObj, identifierMap, gameOutputStream);
                }
            }

            teamOutputStream.close();
            gameOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTeamInsertStatement(JSONObject teamObj, Map<String, Team> teamMap, Map<String, Team> identifierMap, FileOutputStream teamOutputStream) throws Exception {
        String identifier = teamObj.get("teamId").toString();

        String city = teamObj.get("city").toString();
        String mascot = teamObj.get("nickname").toString();

        Team team = getOrCreateTeam(teamMap, teamOutputStream, leagueId, city, mascot);

        identifierMap.put(identifier, team);
    }

    private void createGameInsertStatement(JSONObject gameObj, Map<String, Team> identifierMap, FileOutputStream gameOutputStream) throws Exception {
        String gameIdentifier = gameObj.get("gameId").toString();

        JSONObject homeTeamObj = gameObj.getJSONObject("hTeam");
        String homeTeamId = homeTeamObj.get("teamId").toString();

        JSONObject awayTeamObj = gameObj.getJSONObject("vTeam");
        String awayTeamId = awayTeamObj.get("teamId").toString();

        String dateStr = gameObj.get("startDateEastern").toString(); //20180406
        String timeStr = gameObj.get("startTimeEastern").toString().replace("ET", "").trim(); //10:00 PM ET

        Date dateTime = sdf.parse(dateStr + " " + timeStr);
        long dateTimeMillis = dateTime.getTime();

        Game game = new Game();
        game.setHomeTeamId(identifierMap.get(homeTeamId).getId());
        game.setAwayTeamId(identifierMap.get(awayTeamId).getId());
        game.setDateTime(dateTimeMillis);
        game.setIdentifier(gameIdentifier);
        game.setLeagueId(leagueId);

        createGame(game, gameOutputStream);
    }
}