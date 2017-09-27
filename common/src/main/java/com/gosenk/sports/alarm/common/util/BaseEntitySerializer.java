package com.gosenk.sports.alarm.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gosenk.sports.alarm.common.entity.BaseEntity;

import java.io.IOException;


public class BaseEntitySerializer extends JsonSerializer<BaseEntity> {

    @Override
    public void serialize(BaseEntity arg0, JsonGenerator arg1,
                          SerializerProvider arg2) throws IOException {
        arg1.writeString(arg0.getId());
    }
}