package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateCity;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonCityConverter extends JsonConverter<List<CreateCity>> {
    public JsonCityConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
