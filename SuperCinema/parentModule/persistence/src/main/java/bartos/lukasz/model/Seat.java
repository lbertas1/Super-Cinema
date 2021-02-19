package bartos.lukasz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {
    private Long id;
    private Integer seatNumber;
    private Integer seatRow;
    private Integer place;
    private Long roomId;
    private Long seanceId;
    private String seatStatus;
}
