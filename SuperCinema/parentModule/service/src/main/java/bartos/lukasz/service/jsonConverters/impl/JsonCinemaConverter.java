package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateCinema;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonCinemaConverter extends JsonConverter<List<CreateCinema>> {
    public JsonCinemaConverter(String jsonFilename) {
        super(jsonFilename);
    }
}