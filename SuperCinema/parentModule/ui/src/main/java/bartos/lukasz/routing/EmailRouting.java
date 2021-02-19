package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.getModel.*;
import bartos.lukasz.service.emailServices.EmailService;
import bartos.lukasz.service.emailServices.PdfFileService;
import bartos.lukasz.service.repositoryServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.path;

@Component
@RequiredArgsConstructor
public class EmailRouting {
    private final UserService userService;
    private final CityService cityService;
    private final CinemaService cinemaService;
    private final CinemaRoomService cinemaRoomService;
    private final MovieService movieService;
    private final SeanceService seanceService;
    private final SeatService seatService;
    private final TicketService ticketService;
    private final ReservationService reservationService;
    private final AppCache appCache;

    public void initRoutes() {
        sendEmail();
    }

    public void sendEmail() {
        path("/email", () -> {
            get("/send", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                GetSeance getSeance = seanceService.getSeance(appCache.getProperty("seanceId"));
                GetUser getUser = userService.getUserById(appCache.getProperty("userId"));
                GetCinemaRoom getCinemaRoom = cinemaRoomService.findById(getSeance.getCinemaRoomId());
                GetCinema getCinema = cinemaService.findCinemaById(getCinemaRoom.getCinemaId());
                GetCity getCity = cityService.findById(getCinema.getCityId());
                GetMovie getMovie = movieService.findById(appCache.getProperty("movieId"));
                GetReservation getReservation = reservationService.findById(appCache.getProperty("reservationId"));
                List<GetSeat> getSeats = seatService.getSeats(getReservation.getSeatsId());
                GetTicket getTicket = ticketService.getTicket(appCache.getProperty("ticketId"));

                PdfFileService.createTicket(getTicket, getUser, getSeats, getMovie, getSeance, getCity, getCinema, getCinemaRoom);
                EmailService.send(getUser.getEmail());

                return "Email has been send";
            }));
        });
    }
}
