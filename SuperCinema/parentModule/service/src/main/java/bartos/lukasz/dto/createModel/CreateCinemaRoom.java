package bartos.lukasz.dto.createModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCinemaRoom {
    private Integer roomNumber;
    private Integer quantityOfRows;
    private Integer placesInRow;
    private Long cinemaId;
}
