package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateToWatch;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonToWatchConverter extends JsonConverter<List<CreateToWatch>> {
    public JsonToWatchConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
