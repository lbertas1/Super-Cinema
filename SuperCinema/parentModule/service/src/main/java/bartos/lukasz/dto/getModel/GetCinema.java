package bartos.lukasz.dto.getModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCinema {
    private Long id;
    private String name;
    private Long cityId;
}
