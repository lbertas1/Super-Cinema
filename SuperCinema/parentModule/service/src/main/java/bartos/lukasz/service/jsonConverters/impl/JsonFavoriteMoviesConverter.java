package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateFavoriteMovies;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonFavoriteMoviesConverter extends JsonConverter<List<CreateFavoriteMovies>> {
    public JsonFavoriteMoviesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
