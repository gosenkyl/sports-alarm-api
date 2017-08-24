package com.gosenk.sports.alarm.dataextract;

import com.gosenk.sports.alarm.common.entity.DataReport;
import com.gosenk.sports.alarm.common.repository.DataReportRepository;
import com.gosenk.sports.alarm.dataextract.processor.MLBProcessor;
import com.gosenk.sports.alarm.dataextract.processor.NFLProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.gosenk.sports.alarm.common.repository"})
@ComponentScan("com.gosenk.sports.alarm")
@EntityScan(basePackages = "com.gosenk.sports.alarm.common.entity")
public class DataExtractApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DataExtractApplication.class, args);
    }

    @Autowired
    private NFLProcessor nflProcessor;

    @Autowired
    private MLBProcessor mlbProcessor;

    @Autowired
    private DataReportRepository dataReportRepository;

    @Override
    public void run(String... args) throws Exception {

        String leagues = args[0];

        System.out.println("Arguments " + leagues);

        if(StringUtils.isEmpty(leagues)){
            System.out.println("No Arg Passed!");
            throw new RuntimeException("Need param [nfl,mlb]");
        }

        for(String league : leagues.split(",")) {

            System.out.println(String.format("Processing %s", league));

            DataReport dataReport;

            switch (league.toUpperCase()) {
                case "NFL":
                    dataReport = nflProcessor.process();
                    break;
                case "MLB":
                    dataReport = mlbProcessor.process();
                    break;
                default:
                    throw new RuntimeException(String.format("LEAGUE NOT FOUND %s", league));
            }

            if(dataReport != null){
                dataReportRepository.save(dataReport);
                System.out.println(String.format("PROCESSED [%d NEW] [%d UPDATE] [%d TOTAL] RECORDS FOR %s", dataReport.getInsertCount(), dataReport.getUpdateCount(), dataReport.getTotalCount(), league));
            }
        }
    }
}