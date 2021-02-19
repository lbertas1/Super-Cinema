package bartos.lukasz.dto.createModel;

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
public class CreateTicket {
//    private Long seanceId;
//    private Long seatId;
    private BigDecimal price;
//    private Long userId;
    private TicketType ticketType;
}
