package com.gosenk.sports.alarm.data.processor;

import com.gosenk.sports.alarm.commonlight.entity.GameLight;
import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

abstract class MLBStatsAPIProcessor extends BaseProcessor {

    /**
     * Leagues - http://statsapi.mlb.com/api/v1/sports
     * Teams - http://statsapi.mlb.com/api/v1/teams?sportId=1
     * Schedule - http://statsapi.mlb.com/api/v1/schedule/games/?sportId=1&teamId=116&season=2018&startDate=2018-01-01&endDate=2018-12-31
     */
    private String filePrefix;
    private String leagueId;
    private String season;
    private String seasonStartDate;
    private String seasonEndDate;

    protected static String teamsEndpoint = "http://statsapi.mlb.com/api/v1/teams?sportId={leagueId}";
    protected static String gamesEndpoint = "http://statsapi.mlb.com/api/v1/schedule/games/?sportId={leagueId}&teamId={teamId}&season={season}&startDate={startDate}&endDate={endDate}";

    public MLBStatsAPIProcessor(String filePrefix, String leagueId, String season, String seasonStartDate, String seasonEndDate){
        this.filePrefix = filePrefix;
        this.leagueId = leagueId;
        this.season = season;
        this.seasonStartDate = seasonStartDate;
        this.seasonEndDate = seasonEndDate;
    }

    protected Map<String, TeamLight> processTeams(boolean processAsSQL) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(filePrefix + "_teams.sql");
        outputStream.write(("USE " + schemaName + ";").getBytes());

        Map<String, TeamLight> teamMap = new HashMap<>();

        String str = new RestTemplateBuilder().build().getForObject(teamsEndpoint, String.class, leagueId);

        JSONObject initial = new JSONObject(str);
        JSONArray teams = initial.getJSONArray("teams");

        outputStream.write("\n".getBytes());
        outputStream.write(baseTeamInsert.getBytes());

        int index = 1;
        int teamCount = teams.length();
        for (Object teamObj : teams) {
            outputStream.write("\n".getBytes());

            JSONObject team = (JSONObject) teamObj;

            String id = team.get("id").toString();
            String abbreviation = team.get("abbreviation").toString();
            String locationName = team.get("locationName").toString();
            String teamName= team.get("teamName").toString();
            boolean active = Boolean.valueOf(team.get("active").toString());
            JSONObject venue = team.getJSONObject("venue");
            String venueId = venue.get("id").toString();

            TeamLight entity = new TeamLight();

            entity.setId(id);
            entity.setLeagueId(leagueId);
            entity.setCity(locationName);
            entity.setMascot(teamName);
            entity.setIdentifier(abbreviation);
            entity.setDeleted(!active);
            entity.setNew(true);
            entity.setOriginCity(locationName);
            entity.setOriginMascot(teamName);
            entity.setVenueId(venueId);

            teamMap.put(id, entity);

            boolean isLast = teamCount == index;
            persistTeam(entity, outputStream, isLast);

            index++;
        }

        return teamMap;
    }

    private SimpleDateFormat gameTimeSDF = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");

    protected void processGames(boolean processAsSQL, Map<String, TeamLight> teamMap) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(filePrefix + "_games.sql");
        outputStream.write(("USE " + schemaName + ";").getBytes());

        outputStream.write("\n".getBytes());
        outputStream.write(baseGameInsert.getBytes());
        outputStream.write("\n".getBytes());

        int teamIndex = 1;
        int teamCount = teamMap.size();
        for(String teamId : teamMap.keySet()) {

            String str = new RestTemplateBuilder().build().getForObject(gamesEndpoint, String.class, leagueId, teamId, season, seasonStartDate, seasonEndDate);

            JSONObject initial = new JSONObject(str);
            JSONArray dates = initial.getJSONArray("dates");

            int dateIndex = 1;
            int dateCount = dates.length();
            for(Object dateObj : dates){
                JSONObject date = (JSONObject) dateObj;

                //String dateStr = date.get("date").toString();

                JSONArray games = date.getJSONArray("games");

                int gameIndex = 1;
                int gameCount = games.length();
                for(Object gameObj : games) {
                    JSONObject game = (JSONObject) gameObj;

                    String gameType = game.get("gameType").toString();
                    // Ignore E (Exhibition)
                    if("E".equals(gameType)){
                        continue;
                    }

                    String id = game.get("gamePk").toString();
                    // 2018-02-22T18:05:00Z
                    String gameDate = game.get("gameDate").toString();

                    JSONObject teams = game.getJSONObject("teams");
                    JSONObject away = teams.getJSONObject("away");
                    JSONObject awayTeam = away.getJSONObject("team");
                    JSONObject home = teams.getJSONObject("home");
                    JSONObject homeTeam = home.getJSONObject("team");

                    String awayTeamId = awayTeam.get("id").toString();
                    String homeTeamId = homeTeam.get("id").toString();

                    JSONObject venue = game.getJSONObject("venue");
                    String venueId = venue.get("id").toString();

                    GameLight entity = new GameLight();

                    entity.setId(id);
                    entity.setLeagueId(leagueId);
                    entity.setAwayTeamId(awayTeamId);
                    entity.setHomeTeamId(homeTeamId);
                    entity.setIdentifier(id);
                    entity.setDateTime(gameTimeSDF.parse(gameDate).getTime());
                    entity.setDeleted(false);

                    boolean isLast = teamIndex == teamCount && dateIndex == dateCount && gameIndex == gameCount;

                    persistGame(entity, outputStream, isLast);

                    gameIndex++;
                }
                dateIndex++;
            }
            teamIndex++;
        }
    }


}
