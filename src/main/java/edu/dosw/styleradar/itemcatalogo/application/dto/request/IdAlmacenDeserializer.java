package edu.dosw.styleradar.itemcatalogo.application.dto.request;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import edu.dosw.styleradar.itemcatalogo.domain.model.IdAlmacen;

import java.io.IOException;
import java.util.UUID;

public class IdAlmacenDeserializer extends JsonDeserializer<UUID> {

    @Override
    public UUID deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return IdAlmacen.desdeTexto(parser.getValueAsString());
    }
}
