package bartos.lukasz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    private Long id;
    private Long cityId;
    private Long cinemaId;
    private Long cinemaRoomId;
    private Long movieId;
    private Long seanceId;
    private List<Long> seatsId;
    private Long ticketId;
    private Long userId;
}
