package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.BaseEntity;

import java.util.List;

public interface BaseService<T extends BaseEntity, R> {

    R getRepository();

    T findById(String id);

    List<T> findAll();

    T findOne(String id);

    T save(T dso);

    List<T> save(Iterable<T> dsoList);

    void delete(String id);

    void delete(T dso);

    void delete(List<T> dsoList);

    void deleteAll();

    long count();
}
