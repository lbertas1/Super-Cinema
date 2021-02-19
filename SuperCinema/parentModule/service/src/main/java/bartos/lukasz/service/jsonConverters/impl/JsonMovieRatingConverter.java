package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateMovieRating;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonMovieRatingConverter extends JsonConverter<List<CreateMovieRating>> {
    public JsonMovieRatingConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
