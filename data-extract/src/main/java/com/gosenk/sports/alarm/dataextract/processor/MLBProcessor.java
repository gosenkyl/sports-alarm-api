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
import java.util.Iterator;

@Service
public class MLBProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "MLB";

    private static final String url = "http://gd2.mlb.com/components/game/mlb/year_{year}/month_{month}/day_{day}/master_scoreboard.json";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");

    private League league = null;

    @PostConstruct
    public void setLeague(){
        this.league = getLeague(leagueId);
    }

    public DataReport process() {

        if(league == null){
            throw new RuntimeException(String.format("LEAGUE %s NOT FOUND", leagueId));
        }

        DataReport result = new DataReport();
        result.setLeague(league);
        result.setDate(new Date());

        // TODO Should be command line/gradle param? or database league start/end date?
        String year = "2017";

        // Months
        for(int m = 1; m <= 12; m++){

            String month = StringUtils.leftPad(String.valueOf(m), 2, "0");

            // Days
            for(int d = 1; d <= 31; d++){
                String day = StringUtils.leftPad(String.valueOf(d), 2, "0");

                RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                RestTemplate restTemplate = restTemplateBuilder.build();

                try {
                    String str = restTemplate.getForObject(url, String.class, year, month, day);

                    JSONObject initial = new JSONObject(str);
                    JSONObject data = initial.getJSONObject("data");
                    JSONObject games = data.getJSONObject("games");

                    if (games.has("game")) {
                        result.incTotalCount();

                        if (games.get("game") instanceof JSONArray) {
                            JSONArray gameList = games.getJSONArray("game");
                            Iterator it = gameList.iterator();
                            while (it.hasNext()) {
                                JSONObject gameObj = (JSONObject) it.next();
                                createOrUpdateGame(result, gameObj);
                            }
                        } else {
                            JSONObject gameObj = games.getJSONObject("game");
                            createOrUpdateGame(result, gameObj);
                        }
                    }

                } catch(HttpClientErrorException e){
                    System.out.println(String.format("CANT PROCESS ON %s %s %s", year, month, day));
                } catch(Exception e){
                    System.out.println(String.format("CANT PROCESS ON %s %s %s", year, month, day));
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private void createOrUpdateGame(DataReport result, JSONObject gameObj) throws ParseException {
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

            Game game = getGame(league, gameIdentifier);

            if(game != null){
                if(updateGame(game, dateTimeMillis)){
                    result.incUpdateCount();
                }
            } else {
                String homeTeamCity = gameObj.get("home_team_city").toString();
                String homeTeamMascot = gameObj.get("home_team_name").toString();
                String awayTeamCity = gameObj.get("away_team_city").toString();
                String awayTeamMascot = gameObj.get("away_team_name").toString();

                Team homeTeam = getOrCreateTeam(league, homeIdentifier, homeTeamCity, homeTeamMascot);
                Team awayTeam = getOrCreateTeam(league, awayIdentifier, awayTeamCity, awayTeamMascot);

                game = createGame(league, gameIdentifier, homeTeam, awayTeam, dateTimeMillis);
                result.incInsertCount();
            }

            System.out.println(game.getHomeTeam().getOriginMascot() + " vs " + game.getAwayTeam().getOriginMascot());
        }
    }

}
