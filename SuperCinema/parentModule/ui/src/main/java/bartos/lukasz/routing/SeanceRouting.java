package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateSeance;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class SeanceRouting {

    private final SeanceService seanceService;
    private final GsonService<CreateSeance> gson;
    private final CinemaRoomService cinemaRoomService;
    private final CinemaService cinemaService;
    private final ReservationService reservationService;
    private final MovieService movieService;
    private final TicketService ticketService;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        chooseSeance();
        getSeancesForMovie();
        findSeancesByDate();
    }

    public void save() {
        path("/seances", () -> {
            post("", ((request, response) -> {
                CreateSeance createSeance = gson.getObjectFromJson(request.body(), CreateSeance.class);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return seanceService.save(createSeance);
            }), new JsonTransformer());
        });
    }

    public void getSeancesForMovie() {
        path("/seances/by/movie", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                return seanceService.getSeancesByFilmName(movieService.findById(appCache.getProperty("movieId")).getTitle());
            }), new JsonTransformer());
        });
    }

    public void chooseSeance() {
        path("/seances", () -> {
            get("/:seanceId", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                GetSeance seance = seanceService.getSeance(Long.valueOf(request.params("seanceId")));
                appCache.addOrUpdate("seanceId", seance.getId());
                GetCinemaRoom getCinemaRoom = cinemaRoomService.findById(seance.getCinemaRoomId());
                appCache.addOrUpdate("cinemaRoomId", getCinemaRoom.getId());
                if (!appCache.hasProperty("movieId")) appCache.addOrUpdate("movieId", seance.getMovieId());
                GetCinema cinemaById = cinemaService.findCinemaById(getCinemaRoom.getCinemaId());
                appCache.addOrUpdate("cinemaId", cinemaById.getId());
                appCache.addOrUpdate("cityId", cinemaById.getCityId());

                return seance;
            }), new JsonTransformer());
        });
    }

    public void findSeancesByDate() {
        path("/seances/by/date", () -> {
            get("/:date", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return seanceService.findSeancesByGivenDate(request.params("date"));
            }), new JsonTransformer());
        });
    }
}
