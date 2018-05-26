package com.gosenk.sports.alarm.extract.data.processor;

import com.gosenk.sports.alarm.extract.data.entity.Game;
import com.gosenk.sports.alarm.extract.data.entity.Team;
import com.gosenk.sports.alarm.extract.data.util.XMLParser;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
public class NFLProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "NFL";
    private final static String baseURL = "http://www.nfl.com/ajax/scorestrip?season=:YEAR&seasonType=:TYPE&week=:WEEK";

    public void process(){
        URL url;
        URLConnection connection;

        Map<String, Team> teamMap = new HashMap<>();

        // TODO Make command line params
        String seasonYear = "2017";

        // Only worry about REG season right now
        String[] types = {"REG"}; //{"PRE", "REG"};
        int week = 1;

        try {
            FileOutputStream teamOutputStream = new FileOutputStream("nfl_teams.sql");
            FileOutputStream gameOutputStream = new FileOutputStream("nfl_games.sql");

            String use = "use " + schemaName + ";";
            teamOutputStream.write(use.getBytes());
            gameOutputStream.write(use.getBytes());

            teamOutputStream.write(baseTeamInsert.getBytes());
            gameOutputStream.write(baseGameInsert.getBytes());

            // Loop through season types
            for(String seasonType : types) {
                // Staring with week 1, loop until there are no results for NFL.com
                // Note: Playoffs are strange, investigate
                // Part of regular season? Week 18+? // TODO
                while (true) {
                    String urlStr = baseURL.replace(":YEAR", seasonYear).replace(":TYPE", seasonType).replace(":WEEK", String.valueOf(week));
                    url = new URL(urlStr);
                    connection = url.openConnection();

                    Document doc = XMLParser.parseXML(connection.getInputStream());
                    Node schedule = doc.getElementsByTagName("ss").item(0);
                    Node games = schedule.getFirstChild();

                    if (games == null) {
                        // No more weeks left
                        week = 1;
                        break;
                    }

                    System.out.println(String.format("%s %s WEEK #%s", seasonYear, seasonType, week));

                    NodeList descNodes = games.getChildNodes();

                    /**
                     * EXAMPLE:
                     *
                     *  <gms gd="0" w="1" y="2017" t="R">
                     *      <g eid="2017090700" gsis="57234" d="Thu" t="8:30" q="P" k="" h="NE" hnn="patriots" hs="" v="KC" vnn="chiefs" vs="" p="" rz="" ga="" gt="REG"/>
                     */
                    // "P" = "Pregame"- "H" = "Halftime" - "5" = "Overtime" - "F" = "Final" - "FO" = "Final Overtime";

                    // Loop through the games
                    for (int i = 0; i < descNodes.getLength(); i++) {

                        Node curNode = descNodes.item(i);
                        NamedNodeMap nodeMap = curNode.getAttributes();

                        createInsertStatement(nodeMap, teamMap, teamOutputStream, gameOutputStream);
                    }

                    week++;
                }
            }

            teamOutputStream.close();
            gameOutputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createInsertStatement(NamedNodeMap nodeMap, Map<String, Team> teamMap, FileOutputStream teamOutputStream, FileOutputStream gameOutputStream) {
        String gameIdentifier = nodeMap.getNamedItem("gsis").getNodeValue();

        String eid = nodeMap.getNamedItem("eid").getNodeValue();
        //String day = nodeMap.getNamedItem("d").getNodeValue();
        String time = nodeMap.getNamedItem("t").getNodeValue();
        String amPM = nodeMap.getNamedItem("q").getNodeValue();

        String homeTeamCity = nodeMap.getNamedItem("h").getNodeValue();
        String homeTeamMascot = nodeMap.getNamedItem("hnn").getNodeValue();
        String awayTeamCity = nodeMap.getNamedItem("v").getNodeValue();
        String awayTeamMascot = nodeMap.getNamedItem("vnn").getNodeValue();

        Long dateTimeMillis = parseTime(eid, time, amPM);

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

    private Long parseTime(String eid, String time, String amPM){
        Long dateTimeMillis = null;

        try{
            int year = Integer.parseInt(eid.substring(0, 4));
            int month = Integer.parseInt(eid.substring(4, 6));
            int day = Integer.parseInt(eid.substring(6, 8));

            String[] timeParts = time.split(":");

            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar cal = Calendar.getInstance();

            cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);

            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.MINUTE, minute);

            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if("P".equalsIgnoreCase(amPM)) {
                cal.set(Calendar.AM_PM, Calendar.PM);
            } else {
                cal.set(Calendar.AM_PM, Calendar.AM);
            }

            dateTimeMillis = cal.getTimeInMillis();
        } catch(Exception e){
            e.printStackTrace();
        }

        return dateTimeMillis;
    }

}
