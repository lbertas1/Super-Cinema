package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateTicket;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonTicketConverter extends JsonConverter<List<CreateTicket>> {
    public JsonTicketConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
