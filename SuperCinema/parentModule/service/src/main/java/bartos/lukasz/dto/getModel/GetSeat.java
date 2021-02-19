package bartos.lukasz.dto.getModel;

import bartos.lukasz.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetSeat {
    private Long id;
    private Integer seatNumber;
    private Integer seatRow;
    private Integer place;
    private Long roomId;
    private Long seanceId;
    private SeatStatus seatStatus;
}
