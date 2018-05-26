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
import org.w3c.dom.NamedNodeMap;

import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MLBProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "MLB";

    private final static String baseURL = "http://gd2.mlb.com/components/game/mlb/year_{year}/month_{month}/day_{day}/master_scoreboard.json";

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");

    public void process(){
        Map<String, Team> teamMap = new HashMap<>();

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

            // Months
            for (int m = 1; m <= 12; m++) {

                String month = StringUtils.leftPad(String.valueOf(m), 2, "0");

                // Days
                for (int d = 1; d <= 31; d++) {
                    String day = StringUtils.leftPad(String.valueOf(d), 2, "0");

                    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                    RestTemplate restTemplate = restTemplateBuilder.build();

                    try {
                        String str = restTemplate.getForObject(baseURL, String.class, year, month, day);

                        JSONObject initial = new JSONObject(str);
                        JSONObject data = initial.getJSONObject("data");
                        JSONObject games = data.getJSONObject("games");

                        if (games.has("game")) {
                            if (games.get("game") instanceof JSONArray) {
                                JSONArray gameList = games.getJSONArray("game");
                                for (Object aGameList : gameList) {
                                    JSONObject gameObj = (JSONObject) aGameList;
                                    createInsertStatement(gameObj, teamMap, teamOutputStream, gameOutputStream);
                                }
                            } else {
                                JSONObject gameObj = games.getJSONObject("game");
                                createInsertStatement(gameObj, teamMap, teamOutputStream, gameOutputStream);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(String.format("CANT PROCESS ON %s %s %s", year, month, day));
                        e.printStackTrace();
                    }
                }
            }

            teamOutputStream.close();
            gameOutputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createInsertStatement(JSONObject gameObj, Map<String, Team> teamMap, FileOutputStream teamOutputStream, FileOutputStream gameOutputStream) throws Exception {
        if(gameObj.get("game_type").equals("R")) {
            String homeIdentifier = gameObj.get("home_team_id").toString();
            String awayIdentifier = gameObj.get("away_team_id").toString();
            String date = gameObj.get("original_date").toString();
            String gameNumber = gameObj.get("game_nbr").toString();

            String dateTimeStr = gameObj.get("time_date").toString() + " " + gameObj.get("ampm").toString();
            Date dateTime = sdf.parse(dateTimeStr);
            long dateTimeMillis = dateTime.getTime();

            String[] ids = {homeIdentifier, awayIdentifier, date, gameNumber};
            String gameIdentifier = StringUtils.join(ids, "-");

            String homeTeamCity = gameObj.get("home_team_city").toString();
            String homeTeamMascot = gameObj.get("home_team_name").toString();
            String awayTeamCity = gameObj.get("away_team_city").toString();
            String awayTeamMascot = gameObj.get("away_team_name").toString();

            Team homeTeam = getOrCreateTeam(teamMap, teamOutputStream, leagueId, homeTeamCity, homeTeamMascot);
            Team awayTeam = getOrCreateTeam(teamMap, teamOutputStream, leagueId, awayTeamCity, awayTeamMascot);

            Game game = new Game();
            game.setHomeTeamId(homeTeam.getId());
            game.setAwayTeamId(awayTeam.getId());
            game.setDateTime(dateTimeMillis);
            game.setIdentifier(gameIdentifier);
            game.setLeagueId(leagueId);

            createGame(game, gameOutputStream);
        }
    }

}
