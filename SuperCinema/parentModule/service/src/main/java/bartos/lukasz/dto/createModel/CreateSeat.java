package bartos.lukasz.dto.createModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSeat {
    private Integer seatNumber;
    private Integer seatRow;
    private Integer place;
    private Long roomId;
    private Long seanceId;
}
