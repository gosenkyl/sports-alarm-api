package com.gosenk.sports.alarm.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.gosenk.sports.alarm.common.entity.BaseEntity;
import com.gosenk.sports.alarm.common.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.io.IOException;

@Component
public class BaseEntityDeserializer extends StdDeserializer<BaseEntity> implements ContextualDeserializer {

    @Autowired
    private BaseRepository baseRepository;

    public BaseEntityDeserializer(){
        this(null);
    }

    protected BaseEntityDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public BaseEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (getValueClass() == null) {
            throw new IOException("Unable to deserialize with out class value");
        }

        String id = p.getText();
        try {
            BaseEntity entity = baseRepository.findOne(id);
            //BaseEntity entity = (BaseEntity) entityManager.find(getValueClass(), id);
            return entity;
        } catch (PersistenceException e) {
            throw new IOException("Unable to load BaseModel object for id: "+ id + " and class: "+ getValueClass(), e);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext arg0, BeanProperty arg1) throws JsonMappingException {
        return new BaseEntityDeserializer(arg1.getType().getRawClass());
    }
}