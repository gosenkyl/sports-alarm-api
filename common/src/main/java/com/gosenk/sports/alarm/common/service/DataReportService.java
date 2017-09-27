package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.DataReport;
import com.gosenk.sports.alarm.common.entity.Game;
import com.gosenk.sports.alarm.common.repository.DataReportRepository;
import com.gosenk.sports.alarm.common.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataReportService extends BaseService<DataReport, DataReportRepository>{

    @Autowired
    public DataReportService(DataReportRepository repository){
        super(repository);
    }

}
