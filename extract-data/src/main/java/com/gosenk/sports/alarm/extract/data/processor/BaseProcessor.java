package com.gosenk.sports.alarm.extract.data.processor;

import com.gosenk.sports.alarm.extract.data.entity.Game;
import com.gosenk.sports.alarm.extract.data.entity.Team;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

abstract class BaseProcessor {

    protected final static String schemaName = "sports_alarm";
    protected final static String baseTeamInsert = "INSERT INTO team VALUES (id, league_id, identifier, city, mascot, is_new, origin_city, origin_mascot, image, deleted) ";
    protected final static String baseGameInsert = "INSERT INTO game VALUES (id, home_team_id, away_team_id, date_time, identifier, league_id, deleted) ";

    private Team createTeam(Team team, FileOutputStream teamOutputStream) {
        // Defaults
        team.setNew(true);
        team.setDeleted(false);

        String teamStr = "("
                + surroundWithSingleQuotes(team.getId())
                + surroundWithSingleQuotes(team.getLeagueId())
                + surroundWithSingleQuotes(team.getIdentifier())
                + surroundWithSingleQuotes(team.getCity())
                + surroundWithSingleQuotes(team.getMascot())
                + team.getIsNew() + ","
                + surroundWithSingleQuotes(team.getOriginCity())
                + surroundWithSingleQuotes(team.getOriginMascot())
                + surroundWithSingleQuotes(team.getImage())
                + team.getDeleted()
                + "),";

        System.out.println(teamStr);
        try {
            teamOutputStream.write(teamStr.getBytes());
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return team;
    }

    protected Team getOrCreateTeam(Map<String, Team> teamMap, FileOutputStream teamOutputStream, String leagueId, String city, String mascot){
        String id = leagueId + "-" + city + "-" + mascot;
        Team team = teamMap.get(id);

        if(team == null){
            team = new Team();

            team.setId(id);
            team.setLeagueId(leagueId);
            team.setIdentifier(city);
            team.setCity(city);
            team.setMascot(mascot);
            team.setOriginCity(city);
            team.setOriginMascot(mascot);
            team.setImage(city.toLowerCase());

            createTeam(team, teamOutputStream);
            teamMap.put(id, team);
        }

        return team;
    }

    public String createGame(Game game, FileOutputStream gameOutputStream) {
        String id = UUID.randomUUID().toString();

        // Defaults
        game.setDeleted(false);

        String gameStr = "("
                + surroundWithSingleQuotes(id)
                + surroundWithSingleQuotes(game.getHomeTeamId())
                + surroundWithSingleQuotes(game.getAwayTeamId())
                + game.getDateTime() + ","
                + surroundWithSingleQuotes(game.getIdentifier())
                + surroundWithSingleQuotes(game.getLeagueId())
                + game.getDeleted()
                + "),";

        System.out.println(gameStr);
        try {
            gameOutputStream.write(gameStr.getBytes());
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return id;
    }

    public String surroundWithSingleQuotes(String property){
        return surroundWithSingleQuotes(property, true);
    }

    public String surroundWithSingleQuotes(String property, boolean includeComma){
        String str = "'" + property + "'";

        return includeComma ? str + "," : str;
    }
}
