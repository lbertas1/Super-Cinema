package bartos.lukasz.dto.createModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSeance {
    private Long movieId;
    private String screeningDate;
    private Long cinemaRoomId;
}
