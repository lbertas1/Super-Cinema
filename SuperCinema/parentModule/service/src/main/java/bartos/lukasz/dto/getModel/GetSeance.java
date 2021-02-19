package bartos.lukasz.dto.getModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetSeance {
    private Long id;
    private Long movieId;
    private String movieName;
    private LocalDate screeningDate;
    private Long cinemaRoomId;
}
