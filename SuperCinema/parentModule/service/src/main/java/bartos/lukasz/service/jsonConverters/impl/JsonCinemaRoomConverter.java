package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateCinemaRoom;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonCinemaRoomConverter extends JsonConverter<List<CreateCinemaRoom>> {
    public JsonCinemaRoomConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
