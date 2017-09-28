package com.gosenk.sports.alarm.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.gosenk.sports.alarm.common.entity.BaseEntity;
import com.gosenk.sports.alarm.common.service.LeagueService;
import com.gosenk.sports.alarm.common.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TeamDeserializer extends JsonDeserializer<BaseEntity> {

    @Autowired
    private TeamService service;

    public TeamDeserializer(){}

    @Override
    public BaseEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String id = p.getText();
        try {
            return service.findOne(id);
        } catch (Exception e) {
            throw new IOException("Unable to load object for id: "+ id, e);
        }
    }

}