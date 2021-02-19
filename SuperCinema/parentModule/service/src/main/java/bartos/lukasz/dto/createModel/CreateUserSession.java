package bartos.lukasz.dto.createModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserSession {
    private Long userId;
    private Long cinemaRoomId;
    private Long movieId;
    private Long seanceId;
    private Long seatId;
    private Long ticketId;
}
