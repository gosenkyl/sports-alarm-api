package com.gosenk.sports.alarm.data.processor;

import com.gosenk.sports.alarm.commonlight.entity.GameLight;
import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import com.gosenk.sports.alarm.commonlight.service.GameLightService;
import com.gosenk.sports.alarm.commonlight.service.TeamLightService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;

abstract class BaseProcessor {

    protected boolean processAsSQL = true;

    protected final static String schemaName = "sports_alarm";
    protected final static String baseTeamInsert = "INSERT INTO team (id, league_id, identifier, city, mascot, is_new, origin_city, origin_mascot, venue_id, image, deleted) VALUES ";
    protected final static String baseGameInsert = "INSERT INTO game (id, home_team_id, away_team_id, date_time, identifier, league_id, deleted) VALUES ";

    @Autowired
    protected TeamLightService teamLightService;

    @Autowired
    protected GameLightService gameLightService;

    protected boolean persistTeam(TeamLight team, FileOutputStream outputStream, boolean isLast){
        TeamLight persistedEntity = teamLightService.findById(team.getId());
        boolean teamFound = persistedEntity != null;
        try {
            if (processAsSQL) {
                // TODO
                //if (teamFound && isTeamDirty(persistedEntity, entity)) {
                if(false){
                    updateTeamSQL(persistedEntity, team, outputStream);
                } else {
                    insertTeamSQL(team, outputStream, isLast);
                }
            } else {
                System.out.println("UPDATE NOT IMPLEMENTED! - " + team.getIdentifier());
            }
        } catch(Exception e){
            System.out.println("ERROR PROCESSING TEAM " + team.getIdentifier());
            e.printStackTrace();
        }

        // TODO return fetched team
        return teamFound;
    }

    protected boolean persistGame(GameLight game, FileOutputStream outputStream, boolean isLast){
        GameLight persistedEntity = gameLightService.findById(game.getId());
        boolean gameFound = persistedEntity != null;

        try {
            if (processAsSQL) {
                // TODO
                //if (gameFound && isGameDirty(persistedEntity, entity)) {
                if(false){
                    updateGameSQL(persistedEntity, game, outputStream);
                } else {
                    insertGameSQL(game, outputStream, isLast);
                }
            } else {
                System.out.println("UPDATE NOT IMPLEMENTED! - " + game.getIdentifier());
            }
        } catch(Exception e){
            System.out.println("ERROR PROCESSING GAME " + game.getIdentifier());
            e.printStackTrace();
        }

        return gameFound;
    }

    protected boolean isTeamDirty(TeamLight before, TeamLight after){
        return !before.getIdentifier().equals(after.getIdentifier())
                || !before.getCity().equals(after.getCity())
                || !before.getMascot().equals(after.getMascot())
                || !before.getOriginCity().equals(after.getOriginCity())
                || !before.getOriginMascot().equals(after.getOriginMascot())
                || !before.getImage().equals(after.getImage())
                || !before.getPrimaryColor().equals(after.getPrimaryColor())
                || !before.getSecondaryColor().equals(after.getSecondaryColor())
                || before.getIsNew() != after.getIsNew()
                || before.getDeleted() != after.getDeleted();
    }

    protected void insertTeamSQL(TeamLight entity, FileOutputStream outputStream, boolean isLast) throws Exception {
        String insert = "("
                + surroundWithSingleQuotes(entity.getId())
                + surroundWithSingleQuotes(entity.getLeagueId())
                + surroundWithSingleQuotes(entity.getIdentifier())
                + surroundWithSingleQuotes(entity.getCity())
                + surroundWithSingleQuotes(entity.getMascot())
                + entity.getIsNew() + ","
                + surroundWithSingleQuotes(entity.getOriginCity())
                + surroundWithSingleQuotes(entity.getOriginMascot())
                + surroundWithSingleQuotes(entity.getVenueId())
                + surroundWithSingleQuotes(entity.getImage())
                + entity.getDeleted()
                + ")";

        if(!isLast) { insert += ","; }
        else { insert += ";"; }

        outputStream.write(insert.getBytes());
    }

    protected void updateTeamSQL(TeamLight persistedEntity, TeamLight entity, FileOutputStream outputStream){
        System.out.println("TODO - UPDATE TEAM " + entity.getIdentifier());
    }

    protected boolean isGameDirty(GameLight before, GameLight after){
        return !before.getId().equals(after.getId())
                || !before.getHomeTeamId().equals(after.getHomeTeamId())
                || !before.getAwayTeamId().equals(after.getAwayTeamId())
                || !before.getLeagueId().equals(after.getLeagueId())
                || !before.getIdentifier().equals(after.getIdentifier())
                || !before.getDateTime().equals(after.getDateTime())
                || before.getDeleted() != after.getDeleted();
    }

    protected void insertGameSQL(GameLight entity, FileOutputStream outputStream, boolean isLast) throws Exception {
        String insert = "("
                + surroundWithSingleQuotes(entity.getId())
                + surroundWithSingleQuotes(entity.getHomeTeamId())
                + surroundWithSingleQuotes(entity.getAwayTeamId())
                + entity.getDateTime() + ","
                + surroundWithSingleQuotes(entity.getIdentifier())
                + surroundWithSingleQuotes(entity.getLeagueId())
                + entity.getDeleted()
                + ")";

        if(!isLast) { insert += ","; }
        else { insert += ";"; }

        outputStream.write(insert.getBytes());
    }

    protected void updateGameSQL(GameLight persistedEntity, GameLight entity, FileOutputStream outputStream){
        System.out.println("TODO - UPDATE GAME " + entity.getIdentifier());
    }

    protected String surroundWithSingleQuotes(String property){
        return surroundWithSingleQuotes(property, true);
    }

    protected String surroundWithSingleQuotes(String property, boolean includeComma){
        String str = StringUtils.isNotEmpty(property) ? "'" + property + "'" : null;
        return includeComma ? str + "," : str;
    }
}
