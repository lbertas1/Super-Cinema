package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateSeance;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonSeanceConverter extends JsonConverter<List<CreateSeance>> {
    public JsonSeanceConverter(String jsonFilename) {
        super(jsonFilename);
    }
}