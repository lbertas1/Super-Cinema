package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateMovie;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonMovieConverter extends JsonConverter<List<CreateMovie>> {
    public JsonMovieConverter(String jsonFilename) {
        super(jsonFilename);
    }
}