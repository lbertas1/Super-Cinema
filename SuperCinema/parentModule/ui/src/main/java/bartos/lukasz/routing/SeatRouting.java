package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateReservation;
import bartos.lukasz.dto.createModel.CreateSeat;
import bartos.lukasz.dto.getModel.GetReservation;
import bartos.lukasz.dto.getModel.GetSeat;
import bartos.lukasz.dto.getModel.GetTicket;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.ReservationService;
import bartos.lukasz.service.repositoryServices.SeatService;
import bartos.lukasz.service.repositoryServices.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class SeatRouting {

    private final SeatService seatService;
    private final GsonService<CreateSeat> gson;
    private final AppCache appCache;
    private final ReservationService reservationService;
    private final TicketService ticketService;

    public void initRoutes() {
        save();
        remove();
        selectSeat();
        getSeatsFromSeance();
        busySeatsFromSeance();
        getEmptySeatsFromSeance();
    }

    public void save() {
        path("/seats", () -> {
            post("", ((request, response) -> {
                CreateSeat createSeat = gson.getObjectFromJson(request.body(), CreateSeat.class);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return seatService.save(createSeat);
            }));
        });
    }

    public void remove() {
        path("/seats", () -> {
            delete("/:seatId", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return seatService.removeById(Long.valueOf(request.params("seatId")));
            }));
        });
    }

    public void selectSeat() {
        path("/seats/select/by-number", () -> {
            get("/:number", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                Long seanceId = appCache.getProperty("seanceId");
                GetSeat getSeat = seatService.getSeatByNumber(seanceId, Integer.valueOf(request.params("number")));

                if (getSeat.getSeatStatus().equals(SeatStatus.BUSY)) {
                    halt(406, "Unfortunately the chosen seat is not empty. " +
                            "Download empty seats again, and select other place.");
                }

                if (!appCache.hasProperty("reservationId")) {
                    GetTicket ticket = ticketService.createTicket();
                    appCache.addOrUpdate("ticketId", ticket.getId());

                    List<Long> seats = new ArrayList<>();
                    seats.add(getSeat.getId());

                    CreateReservation newReservation = CreateReservation
                            .builder()
                            .cityId(appCache.getProperty("cityId"))
                            .cinemaId(appCache.getProperty("cinemaId"))
                            .cinemaRoomId(appCache.getProperty("cinemaRoomId"))
                            .movieId(appCache.getProperty("movieId"))
                            .seanceId(seanceId)
                            .userId(appCache.getProperty("userId"))
                            .seatsId(seats)
                            .ticketId(ticket.getId())
                            .build();

                    GetReservation saved = reservationService.save(newReservation);
                    appCache.addOrUpdate("reservationId", saved.getId());
                } else {
                    GetReservation getReservation = reservationService.findById(appCache.getProperty("reservationId"));
                    getReservation.getSeatsId().add(getSeat.getId());
                    reservationService.update(getReservation);
                }

                return Integer.valueOf(request.params("number"));
            }));
        });
    }

    public void getSeatsFromSeance() {
        path("/seats/by-seance", () -> {
            get("/:seanceId", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return seatService.getSeatsBySeanceId(Long.valueOf(request.params("seanceId")));
            }));
        });
    }

    public void busySeatsFromSeance() {
        path("/seats/busy", () -> {
            get("/:seanceId", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return seatService.getBusySeatsFromSeance(Long.valueOf(request.params("seanceId")));
            }));
        });
    }

    public void getEmptySeatsFromSeance() {
        path("/seats/empty", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                Long seanceId = appCache.getProperty("seanceId");
                return seatService.getAllSeatsByStatus(seanceId, SeatStatus.EMPTY);
            }), new JsonTransformer());
        });
    }
}
