package com.gosenk.sports.alarm.common.service;

import com.gosenk.sports.alarm.common.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public abstract class BaseServiceImpl<T extends BaseEntity, R extends CrudRepository<T, String>> implements BaseService<T, R> {

    private final R repository;

    @SuppressWarnings("WeakerAccess")
    public BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    public R getRepository() {
        return repository;
    }

    public T findById(String id) {
        if (id == null) {
            return null;
        } else {
            return getRepository().findById(id).orElse(null);
        }
    }

    public List<T> findAll() {
        return (List<T>) getRepository().findAll();
    }

    /*public List<T> findAll(){
        return (List) getRepository().findAll();
    }

    public Page<T> findAll(Pageable pageable){
        return getRepository().findAll(pageable);
    }*/

    public T findOne(String id){
        return getRepository().findById(id).orElse(null);
    }

    public T save(T dso) {
        if (dso.getId() == null) {
            dso.setId(UUID.randomUUID().toString());
        }
        return getRepository().save(dso);
    }

    public List<T> save(Iterable<T> dsoList){
        return (List<T>) getRepository().saveAll(dsoList);
    }

    public void delete(String id){
        getRepository().deleteById(id);
    }

    public void delete(T dso) {
        getRepository().delete(dso);
    }

    public void delete(List<T> dsoList){
        getRepository().deleteAll(dsoList);
    }

    public void deleteAll(){
        getRepository().deleteAll();
    }

    public long count(){
        return getRepository().count();
    }

}
