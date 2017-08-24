package com.gosenk.sports.alarm.dataextract.processor;

import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.common.repository.GameRepository;
import com.gosenk.sports.alarm.common.repository.LeagueRepository;
import com.gosenk.sports.alarm.common.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseProcessor {

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    protected League getLeague(String id){
        return leagueRepository.findOne(id);
    }

    protected Team getOrCreateTeam(League league, String identifier, String city, String mascot){
        String leagueId = league.getId();
        List<Team> teams = teamRepository.findByLeagueIdentifier(leagueId, identifier);

        if(teams != null && teams.size() > 0){
            if(teams.size() > 1){
                System.out.println(String.format("MORE THAN 1 TEAM FOUND FOR %s %s %s", leagueId, city, mascot));
            }

            // TODO - Is there a need to check for City/Mascot change or is a new team created?
            return teams.get(0);
        }

        System.out.println(String.format("CREATING NEW TEAM FOR %s %s %s", leagueId, city, mascot));

        Team team = new Team();

        team.setNew(true);
        team.setLeague(league);
        team.setIdentifier(identifier);

        // TODO - Remove these 2 lines, should be manual for NEW teams to fix things like Chi White Sox as city for Chicago - White Sox
        team.setCity(city);
        team.setMascot(mascot);

        team.setOriginCity(city);
        team.setOriginMascot(mascot);

        teamRepository.save(team);

        return team;
    }

    protected Game getGame(League league, String identifier){
        List<Game> games = gameRepository.findByLeagueIdentifier(league.getId(), identifier);
        if(games != null && games.size() > 0){
            if(games.size() > 1){
                System.out.println(String.format("MORE THAN 1 GAME FOUND FOR %s %s", league.getId(), identifier));
            }

            return games.get(0);
        }

        return null;
    }

    protected Game createGame(League league, String gameIdentifier, Team homeTeam, Team awayTeam, long dateTime){
        Game game = new Game();

        game.setLeague(league);
        game.setIdentifier(gameIdentifier);
        game.setHomeTeam(homeTeam);
        game.setAwayTeam(awayTeam);
        game.setDateTime(dateTime);

        gameRepository.save(game);

        return game;
    }

    /**
     * Updates the game if necessary, returns true if there was something to update.
     * @param game
     * @param dateTime
     * @return
     */
    protected boolean updateGame(Game game, long dateTime){
        // Date/Time is only dirty condition for now, teams should never change
        if(game.getDateTime().equals(dateTime) == false){
            game.setDateTime(dateTime);

            gameRepository.save(game);
            return true;
        }

        return false;
    }

}
