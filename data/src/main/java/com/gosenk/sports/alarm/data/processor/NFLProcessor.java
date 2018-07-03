package com.gosenk.sports.alarm.data.processor;

import com.gosenk.sports.alarm.commonlight.entity.GameLight;
import com.gosenk.sports.alarm.commonlight.entity.TeamLight;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class NFLProcessor extends BaseProcessor implements Processor {

    private final static String filePrefix = "nfl";
    private final static String leagueId = "NFL";
    private final static String season = "2018";
    private final static String seasonStartDate = "2018-06-01";
    private final static String seasonEndDate = "2018-05-31";

    private final static String baseURL = "http://www.nfl.com/ajax/scorestrip?season=:YEAR&seasonType=:TYPE&week=:WEEK";

    public void process(boolean processAsSQL){
        URL url;
        URLConnection connection;

        Map<String, TeamLight> teamMap = new HashMap<>();

        // Only worry about REG season right now
        String[] types = {"REG"}; //{"PRE", "REG"};
        int week = 1;

        try {
            FileOutputStream teamOutputStream = new FileOutputStream(filePrefix + "_teams.sql");
            FileOutputStream gameOutputStream = new FileOutputStream(filePrefix + "_games.sql");

            String use = "USE " + schemaName + ";";
            teamOutputStream.write(use.getBytes());
            gameOutputStream.write(use.getBytes());
            teamOutputStream.write("\n".getBytes());
            gameOutputStream.write("\n".getBytes());

            teamOutputStream.write(baseTeamInsert.getBytes());
            gameOutputStream.write(baseGameInsert.getBytes());
            teamOutputStream.write("\n".getBytes());
            gameOutputStream.write("\n".getBytes());

            // Loop through season types
            for(String seasonType : types) {
                // Staring with week 1, loop until there are no results for NFL.com
                // Note: Playoffs are strange, investigate
                // Part of regular season? Week 18+? // TODO
                while (true) {
                    String urlStr = baseURL.replace(":YEAR", season).replace(":TYPE", seasonType).replace(":WEEK", String.valueOf(week));
                    url = new URL(urlStr);
                    connection = url.openConnection();

                    Document doc = parseXML(connection.getInputStream());
                    Node schedule = doc.getElementsByTagName("ss").item(0);
                    Node games = schedule.getFirstChild();

                    if (games == null) {
                        // No more weeks left
                        week = 1;
                        break;
                    }

                    System.out.println(String.format("%s %s WEEK #%s", season, seasonType, week));

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

                        boolean isLast = descNodes.getLength() -1 == i;
                        createOrUpdateTeamsGame(nodeMap, teamMap, teamOutputStream, gameOutputStream, isLast);
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

    private void createOrUpdateTeamsGame(NamedNodeMap nodeMap, Map<String, TeamLight> teamMap, FileOutputStream teamOutputStream, FileOutputStream gameOutputStream, boolean isLast) throws Exception {
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

        String homeTeamId = (leagueId + "-" + homeTeamCity + "-" + homeTeamMascot).toUpperCase();
        TeamLight homeTeam = teamMap.get(homeTeamId);

        if(homeTeam == null) {
            homeTeam = createTeam(homeTeamId, homeTeamCity, homeTeamMascot);

            // Since we can only infer teams by looking at games,
            // we don't know if we are done processing teams or not
            persistTeam(homeTeam, teamOutputStream, false);
            teamOutputStream.write("\n".getBytes());

            teamMap.put(homeTeamId, homeTeam);
        }

        String awayTeamId = (leagueId + "-" + awayTeamCity + "-" + awayTeamMascot).toUpperCase();
        TeamLight awayTeam = teamMap.get(awayTeamId);

        if(awayTeam == null){
            awayTeam = createTeam(awayTeamId, awayTeamCity, awayTeamMascot);

            // Since we can only infer teams by looking at games,
            // we don't know if we are done processing teams or not
            persistTeam(awayTeam, teamOutputStream, false);
            teamOutputStream.write("\n".getBytes());

            teamMap.put(awayTeamId, awayTeam);
        }

        GameLight game = new GameLight();
        game.setHomeTeamId(homeTeam.getId());
        game.setAwayTeamId(awayTeam.getId());
        game.setDateTime(dateTimeMillis);
        game.setIdentifier(gameIdentifier);
        game.setLeagueId(leagueId);

        persistGame(game, gameOutputStream, isLast);
    }

    private TeamLight createTeam(String teamId, String city, String mascot){
        TeamLight team = new TeamLight();

        team.setId(teamId);
        team.setLeagueId(leagueId);
        team.setCity(city);
        team.setMascot(mascot);
        team.setIdentifier(city);
        team.setDeleted(false); // TODO
        team.setNew(true); // TODO
        team.setOriginCity(city);
        team.setOriginMascot(mascot);
        team.setVenueId(null);

        return team;
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

    public static Document parseXML(InputStream stream) throws Exception {

        DocumentBuilderFactory objDocumentBuilderFactory;
        DocumentBuilder objDocumentBuilder;
        Document doc;
        try {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        return doc;
    }
}
