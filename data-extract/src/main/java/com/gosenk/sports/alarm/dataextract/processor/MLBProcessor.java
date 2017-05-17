package com.gosenk.sports.alarm.dataextract.processor;

import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.common.object.ProcessorResult;
import com.gosenk.sports.alarm.common.repository.GameRepository;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

@Service
public class MLBProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "MLB";

    private static final String url = "http://gd2.mlb.com/components/game/mlb/year_{year}/month_{month}/day_{day}/master_scoreboard.json";

    private League league = null;

    @PostConstruct
    public void setLeague(){
        this.league = getLeague(leagueId);
    }

    public ProcessorResult process() {


        if(league == null){
            throw new RuntimeException(String.format("LEAGUE %s NOT FOUND", leagueId));
        }

        ProcessorResult result = new ProcessorResult(league.getId());

        // TODO Should be command line/gradle param?
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

                    //System.out.println(str);

                    JSONObject initial = new JSONObject(str);
                    JSONObject data = initial.getJSONObject("data");
                    JSONObject games = data.getJSONObject("games");

                    if(games.has("game")) {
                        long dateTime = 0; // TODO

                        if(games.get("game") instanceof JSONArray){
                            JSONArray gameList = games.getJSONArray("game");
                            Iterator it = gameList.iterator();
                            while (it.hasNext()) {
                                JSONObject gameObj = (JSONObject) it.next();
                                createOrUpdateGame(result, gameObj, dateTime);
                            }
                        } else {
                            JSONObject gameObj = games.getJSONObject("game");
                            createOrUpdateGame(result, gameObj, dateTime);
                        }
                    }

                } catch(Exception e){
                    System.out.println(String.format("CANT PROCESS ON %s %s %s", year, month, day));
                }

            }

        }

        return result;
    }

    private void createOrUpdateGame(ProcessorResult result, JSONObject gameObj, long dateTime) throws ParseException {
        if(gameObj.get("game_type").equals("R")) {
            String homeIdentifier = gameObj.get("home_team_id").toString();
            String awayIdentifier = gameObj.get("away_team_id").toString();
            String date = gameObj.get("original_date").toString();
            String gameNumber = gameObj.get("game_nbr").toString();

            String gameIdentifier = String.join(homeIdentifier, awayIdentifier, date, gameNumber, "-");

            Game game = getGame(league, gameIdentifier);

            if(game != null){
                if(updateGame(game, dateTime)){
                    result.incUpdateCount();
                }
            } else {
                game = createGame(league, gameIdentifier, homeIdentifier, awayIdentifier, dateTime);
                result.incInsertCount();
            }

            System.out.println(game.getHomeTeam().getId() + " vs " + game.getAwayTeam().getId());
        }
    }

}
