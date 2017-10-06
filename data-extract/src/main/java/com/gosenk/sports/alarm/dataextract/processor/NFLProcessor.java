package com.gosenk.sports.alarm.dataextract.processor;

import com.gosenk.sports.alarm.common.entity.DataReport;
import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.entity.League;
import com.gosenk.sports.alarm.common.entity.Team;
import com.gosenk.sports.alarm.dataextract.util.XMLParser;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class NFLProcessor extends BaseProcessor implements Processor {

    private final static String leagueId = "NFL";

    private static String PREGAME = "Pregame";
    private static String HALFTIME = "Halftime";
    private static String OVERTIME= "Overtime";
    private static String FINAL = "Final";
    private static String FINAL_OT = "Final Overtime";

    private static String baseURL = "http://www.nfl.com/ajax/scorestrip?season=:YEAR&seasonType=:TYPE&week=:WEEK";

    private League league = null;

    @PostConstruct
    public void setLeague(){
        this.league = getLeague(leagueId);
    }

    public DataReport process(){

        if(league == null){
            throw new RuntimeException(String.format("LEAGUE %s NOT FOUND", leagueId));
        }

        DataReport result = new DataReport();
        result.setLeague(league);
        result.setDate(new Date());

        URL url;
        URLConnection connection;

        // TODO Make command line params
        String seasonYear = "2017";

        // Only worry about REG season right now
        String[] types = {"REG"}; //{"PRE", "REG"};
        int week = 1;

        try {

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

                        String gameIdentifier = nodeMap.getNamedItem("gsis").getNodeValue();

                        String eid = nodeMap.getNamedItem("eid").getNodeValue();
                        //String day = nodeMap.getNamedItem("d").getNodeValue();
                        String time = nodeMap.getNamedItem("t").getNodeValue();
                        String amPM = nodeMap.getNamedItem("q").getNodeValue();

                        Long dateTimeMillis = parseTime(eid, time, amPM);

                        Game game = getGame(league, gameIdentifier);

                        result.incTotalCount();

                        // Make sure nothing changed
                        if(game != null){
                            if(updateGame(game, dateTimeMillis)){
                                result.incUpdateCount();
                            }
                        } else {
                            String homeTeamCity = nodeMap.getNamedItem("h").getNodeValue();
                            String homeTeamMascot = nodeMap.getNamedItem("hnn").getNodeValue();
                            String awayTeamCity = nodeMap.getNamedItem("v").getNodeValue();
                            String awayTeamMascot = nodeMap.getNamedItem("vnn").getNodeValue();

                            Team homeTeam = getOrCreateTeam(league, homeTeamCity, homeTeamCity, homeTeamMascot);
                            Team awayTeam = getOrCreateTeam(league, awayTeamCity, awayTeamCity, awayTeamMascot);

                            game = createGame(league, gameIdentifier, homeTeam, awayTeam, dateTimeMillis);
                            result.incInsertCount();
                        }

                        System.out.println(game.getHomeTeam().getIdentifier() + " vs " + game.getAwayTeam().getIdentifier());
                    }

                    week++;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
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

    /*private void currentWeek() throws Exception {
        // Current NFL week
        URL url = new URL("http://www.nfl.com/liveupdate/scorestrip/ss.xml");
        URLConnection connection = url.openConnection();
        Document curDoc = XMLParser.parseXML(connection.getInputStream());
        NodeList curSchedule = curDoc.getElementsByTagName("gms");

        NamedNodeMap curNodeMap = curSchedule.item(0).getAttributes();

        String curWeek = curNodeMap.getNamedItem("w").getNodeValue();
        String curYear = curNodeMap.getNamedItem("y").getNodeValue();

        System.out.println("Current Week: " + curWeek + " Year: " + curYear);
    }*/

}
