package me.wired.learning.user;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class XUserSerializer extends JsonSerializer<XUser> {

    @Override
    public void serialize(XUser xUser, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("id", xUser.getId());

        gen.writeEndObject();
    }

}
