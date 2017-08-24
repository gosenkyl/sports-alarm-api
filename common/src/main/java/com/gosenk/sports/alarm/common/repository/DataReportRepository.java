package com.gosenk.sports.alarm.common.repository;

import com.gosenk.sports.alarm.common.entity.DataReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataReportRepository extends CrudRepository<DataReport, String>{

}
