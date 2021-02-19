package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateCity;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.dto.getModel.GetCity;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class CityRouting {
    private final CityService cityService;
    private final GsonService<CreateCity> gson;
    private final CinemaService cinemaService;
    private final CinemaRoomService cinemaRoomService;
    private final SeanceService seanceService;
    private final MovieService movieService;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        getAllCities();
        getRepertoireFromCity();
    }

    public void save() {
        path("/cities", () -> {
            post("", ((request, response) -> {
                var newCity = gson.getObjectFromJson(request.body(), CreateCity.class);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return cityService.save(newCity);
            }), new JsonTransformer());
        });
    }

    public void getAllCities() {
        path("/cities/all", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return cityService.getCities();
            }), new JsonTransformer());
        });
    }

    public void getRepertoireFromCity() {
        path("/cities/repertoire", () -> {
            get("/:cityName", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                GetCity getCity = cityService.getCityByName(request.params("cityName"));
                List<GetCinema> cinemaFromCity = cinemaService.getCinemaFromCity(getCity.getName());
                List<GetCinemaRoom> cinemaRoomsFromCinemaList = cinemaRoomService.getCinemaRoomsFromCinemaList(cinemaFromCity);
                List<GetSeance> seancesByCinemaRoomsId = seanceService.getSeancesByCinemaRoomsId(cinemaRoomsFromCinemaList);

                appCache.addOrUpdate("cityId", getCity.getId());

                return movieService.getAllByMoviesIdList(seancesByCinemaRoomsId);
            }), new JsonTransformer());
        });
    }
}
