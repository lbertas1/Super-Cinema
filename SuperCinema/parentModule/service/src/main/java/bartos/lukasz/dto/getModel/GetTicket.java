package bartos.lukasz.dto.getModel;

import bartos.lukasz.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTicket {
    private Long id;
//    private Long seanceId;
//    private Long seatId;
    private BigDecimal price;
//    private Long userId;
    private TicketType ticketType;
}
