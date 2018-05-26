package com.gosenk.sports.alarm.extract.data;

import com.gosenk.sports.alarm.extract.data.processor.MLBProcessor;
import com.gosenk.sports.alarm.extract.data.processor.NBAProcessor;
import com.gosenk.sports.alarm.extract.data.processor.NFLProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gosenk.sports.alarm.extract.data")
public class ExtractDataApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ExtractDataApplication.class, args);
    }

    @Autowired
    private NFLProcessor nflProcessor;

    @Autowired
    private MLBProcessor mlbProcessor;

    @Autowired
    private NBAProcessor nbaProcessor;

    @Override
    public void run(String... args) throws Exception {
        nflProcessor.process();
        mlbProcessor.process();
        nbaProcessor.process();
    }
}