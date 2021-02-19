package bartos.lukasz.dto.getModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCinemaRoom {
    private Long id;
    private Integer roomNumber;
    private Integer quantityOfRows;
    private Integer placesInRow;
    private Long cinemaId;
}
