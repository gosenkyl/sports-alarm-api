package com.gosenk.sports.alarm.dataextract;

import com.gosenk.sports.alarm.dataextract.processor.MLBProcessor;
import com.gosenk.sports.alarm.dataextract.processor.NFLProcessor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void run(String... args) throws Exception {
        if(StringUtils.isEmpty(args)){
            throw new RuntimeException("Need param [nfl,mlb]");
        }

        String arg1 = "mlb"; //args[0];

        System.out.println(String.format("Processing %s", arg1));

        int results;

        switch(arg1){
            case "nfl":
                results = processNFL();
                break;
            case "mlb":
                results = processMLB();
                break;
            default:
                throw new RuntimeException("param not nfl/mlb");
        }

        System.out.println(String.format("PROCESSED %s RECORDS FOR %s", results, arg1));
    }

    private int processNFL(){
        nflProcessor.process();

        return 0;
    }

    private int processMLB(){
        mlbProcessor.process();

        return 0;
    }

}