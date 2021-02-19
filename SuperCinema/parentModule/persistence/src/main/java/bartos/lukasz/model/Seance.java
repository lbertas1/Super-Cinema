package bartos.lukasz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seance {
    private Long id;
    private Long movieId;
    private LocalDate screeningDate;
    private Long cinemaRoomId;
}
