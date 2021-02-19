package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateReservation;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonReservationConverter extends JsonConverter<List<CreateReservation>> {
    public JsonReservationConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
