package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateTicket;
import bartos.lukasz.dto.getModel.GetTicket;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.ReservationService;
import bartos.lukasz.service.repositoryServices.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class TicketRouting {
    private final TicketService ticketService;
    private final GsonService<CreateTicket> gson;
    private final AppCache appCache;
    private final ReservationService reservationService;

    public void initRoutes() {
        save();
        getPayment();
        postPayment();
    }

    public void save() {
        path("/tickets", () -> {
            post("", ((request, response) -> {
                CreateTicket createTicket = gson.getObjectFromJson(request.body(), CreateTicket.class);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return ticketService.save(createTicket);
            }), new JsonTransformer());
        });
    }

    public void getPayment() {
        path("/tickets/payment/get", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                GetTicket getTicket = ticketService.getTicket(appCache.getProperty("ticketId"));
                int seatsNumber = reservationService.findById(appCache.getProperty("reservationId")).getSeatsId().size();
                BigDecimal price = ticketService.countTicketPrice(getTicket.getPrice(), seatsNumber);
                getTicket.setPrice(price);
                ticketService.updateTicket(getTicket.getId(), getTicket);
                return "To pay for ticket you should enter: " + price;
            }), new JsonTransformer());
        });
    }

    public void postPayment() {
        path("/tickets/payment/pay", () -> {
            post("/:payment", ((request, response) -> {
                String payment = request.params("payment");

                Long ticketId = appCache.getProperty("ticketId");
                GetTicket ticket = ticketService.getTicket(ticketId);

                if (ticket.getPrice().equals(new BigDecimal(payment))) {
                    ticket.setTicketType(TicketType.O);
                    ticketService.updateTicket(ticket.getId(), ticket);
                    response.header("Content-Type", "application/json;charset=utf-8");
                    response.status(201);
                    return ticket;
                } else {
                    response.header("Content-Type", "application/json;charset=utf-8");
                    response.status(406);
                    ticket.setTicketType(TicketType.R);
                    ticketService.updateTicket(ticket.getId(), ticket);
                    return "You provide incorrect amount " + payment;
                }
            }), new JsonTransformer());
        });
    }
}

