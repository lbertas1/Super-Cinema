package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateCinema;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.CinemaRoomService;
import bartos.lukasz.service.repositoryServices.CinemaService;
import bartos.lukasz.service.repositoryServices.MovieService;
import bartos.lukasz.service.repositoryServices.SeanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class CinemaRouting {

    private final CinemaService cinemaService;
    private final GsonService<CreateCinema> gson;
    private final CinemaRoomService cinemaRoomService;
    private final MovieService movieService;
    private final SeanceService seanceService;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        getCinemaByName();
        getCinemasFromCity();
        getAllCinemas();
        getRepertoireFromChosenCinema();
    }

    public void save() {
        path("/cinemas", () -> {
            post("", ((request, response) -> {
                var newCinema = gson.getObjectFromJson(request.body(), CreateCinema.class);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return cinemaService.save(newCinema);
            }), new JsonTransformer());
        });
    }

    public void getCinemaByName() {
        path("/cinemas/name", () -> {
            get("/:cinemaName", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                GetCinema getCinema = cinemaService.getCinemaByName(request.params("cinemaName"));
                appCache.addOrUpdate("cinemaId", getCinema.getId());
                return getCinema;
            }), new JsonTransformer());
        });
    }

    public void getCinemasFromCity() {
        path("/cinemas/city", () -> {
            get("/:cityName", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return cinemaService.getCinemaFromCity(request.params("cityName"));
            }), new JsonTransformer());
        });
    }

    public void getAllCinemas() {
        path("/cinemas/all", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return cinemaService.getAllCinemas();
            }), new JsonTransformer());
        });
    }

    public void getRepertoireFromChosenCinema() {
        path("/cinemas/repertoire", () -> {
            get("/:cinemaName", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                GetCinema getCinema = cinemaService.getCinemaByName(request.params("cinemaName"));
                List<GetCinemaRoom> cinemaRoomsByCinemaId = cinemaRoomService.getCinemaRoomsByCinemaId(getCinema);
                List<GetSeance> seancesByCinemaRoomsId = seanceService.getSeancesByCinemaRoomsId(cinemaRoomsByCinemaId);
                return movieService.getAllByMoviesIdList(seancesByCinemaRoomsId);
            }), new JsonTransformer());
        });
    }
}
