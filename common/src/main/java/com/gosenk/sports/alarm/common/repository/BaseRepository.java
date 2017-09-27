package com.gosenk.sports.alarm.common.repository;

import com.gosenk.sports.alarm.common.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository extends CrudRepository<BaseEntity, String>{


}
