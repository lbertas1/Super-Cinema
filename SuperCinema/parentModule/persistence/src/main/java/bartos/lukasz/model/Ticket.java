package bartos.lukasz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    private Long id;
//    private Long seanceId;
//    private Long seatId;
    private BigDecimal price;
//    private Long userId;
    private String ticketType;
}
