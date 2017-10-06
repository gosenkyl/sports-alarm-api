package com.gosenk.sports.alarm.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.gosenk.sports.alarm.common.entity.BaseEntity;
import com.gosenk.sports.alarm.common.service.ApplicationContextService;
import com.gosenk.sports.alarm.common.service.BaseService;

import java.io.IOException;

public class BaseEntityDeserializer extends StdDeserializer<BaseEntity> implements ContextualDeserializer {

    public BaseEntityDeserializer() {
        this(null);
    }

    protected BaseEntityDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BaseEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String id = p.getText();
        try {
            String entityName = handledType().getSimpleName();
            String beanName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1, entityName.length()) + "Service";

            BaseService baseService = (BaseService) ApplicationContextService.getApplicationContext().getAutowireCapableBeanFactory().getBean(beanName);

            return baseService.findOne(id);
        } catch (Exception e) {
            throw new IOException("Unable to load object for id: "+ id + " and class: " + handledType(), e);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext arg0,
                                                BeanProperty arg1) throws JsonMappingException {
        return new BaseEntityDeserializer(arg1.getType().getRawClass());
    }
}