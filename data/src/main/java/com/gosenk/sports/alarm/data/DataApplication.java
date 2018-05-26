package com.gosenk.sports.alarm.data;

import com.gosenk.sports.alarm.data.processor.MLBProcessor;
import com.gosenk.sports.alarm.data.processor.NBAProcessor;
import com.gosenk.sports.alarm.data.processor.NFLProcessor;
import com.gosenk.sports.alarm.data.processor.NHLProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.gosenk.sports.alarm.common.repository", "com.gosenk.sports.alarm.commonlight.repository"})
@ComponentScan("com.gosenk.sports.alarm")
@EntityScan(basePackages = {"com.gosenk.sports.alarm.common.entity", "com.gosenk.sports.alarm.commonlight.entity"})
public class DataApplication implements CommandLineRunner {

    @Autowired
    private MLBProcessor mlbProcessor;

    @Autowired
    private NBAProcessor nbaProcessor;

    @Autowired
    private NHLProcessor nhlProcessor;

    @Autowired
    private NFLProcessor nflProcessor;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO - Parameterize
        boolean isInsertStatements = true;

        mlbProcessor.process(isInsertStatements);
        nbaProcessor.process(isInsertStatements);
        nhlProcessor.process(isInsertStatements);
        nflProcessor.process(isInsertStatements);
    }
}
