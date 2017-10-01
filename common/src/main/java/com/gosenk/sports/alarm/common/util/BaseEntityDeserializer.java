package com.gosenk.sports.alarm.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.gosenk.sports.alarm.common.entity.BaseEntity;
import com.gosenk.sports.alarm.common.service.BaseService;
import com.gosenk.sports.alarm.common.service.TeamService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.logging.log4j2.SpringBootConfigurationFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class BaseEntityDeserializer extends StdDeserializer<BaseEntity> implements ContextualDeserializer {

    public BaseEntityDeserializer() {
        this(null);
    }

    protected BaseEntityDeserializer(Class<?> vc) {
        super(vc);
    }

    private ApplicationContext applicationContext;

    @Override
    public BaseEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String id = p.getText();
        try {
            /*BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
            BeanFactoryReference bf = bfl.useBeanFactory("SOME FACTORY");
            BaseService baseService = (BaseService) bf.getFactory().getBean(handledType() + "Service");*/

            //BaseService baseService = (BaseService) applicationContext.getAutowireCapableBeanFactory().getBean(handledType() + "Service");

            BaseService baseService = (BaseService) applicationContext.getBean(handledType() + "Service");

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