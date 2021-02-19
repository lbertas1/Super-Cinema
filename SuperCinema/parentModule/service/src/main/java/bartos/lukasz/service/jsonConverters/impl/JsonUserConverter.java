package bartos.lukasz.service.jsonConverters.impl;

import bartos.lukasz.dto.createModel.CreateUser;
import bartos.lukasz.service.jsonConverters.JsonConverter;

import java.util.List;

public class JsonUserConverter extends JsonConverter<List<CreateUser>> {
    public JsonUserConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
