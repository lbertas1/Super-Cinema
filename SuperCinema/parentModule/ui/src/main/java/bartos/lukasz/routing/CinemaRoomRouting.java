package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateCinemaRoom;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.CinemaRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class CinemaRoomRouting {

    private final CinemaRoomService cinemaRoomService;
    private final GsonService<CreateCinemaRoom> gson;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        getCinemaRoomsFromCinema();
    }

    public void save() {
        path("/cinema-rooms", () -> {
            post("", ((request, response) -> {
                var newCinemaRoom = gson.getObjectFromJson(request.body(), CreateCinemaRoom.class);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return cinemaRoomService.save(newCinemaRoom);
            }), new JsonTransformer());
        });
    }

    public void getCinemaRoomsFromCinema() {
        path("/cinema-rooms", () -> {
            get("/:cinemaName", ((request, response) -> {
                var cinemaName = request.params("cinemaName");
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return cinemaRoomService.getCinemaRoomsByCinemaName(cinemaName);
            }), new JsonTransformer());
        });
    }
}
