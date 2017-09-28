package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.DataReport;
import com.gosenk.sports.alarm.common.repository.DataReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataReportService extends BaseServiceImpl<DataReport, DataReportRepository> {

    @Autowired
    public DataReportService(DataReportRepository repository){
        super(repository);
    }

}
