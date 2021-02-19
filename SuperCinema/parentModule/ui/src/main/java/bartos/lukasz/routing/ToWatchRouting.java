package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateToWatch;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.ToWatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class ToWatchRouting {

    private final ToWatchService toWatchService;
    private final GsonService<CreateToWatch> gson;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        remove();
        getAllMovies();
        getAllByType();
    }

    public void save() {
        path("/to-watch", () -> {
            post("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return toWatchService.save(gson.getObjectFromJson(request.body(), CreateToWatch.class));
            }), new JsonTransformer());
        });
    }

    public void remove() {
        path("/to-watch/remove", () -> {
            delete("/:movieName", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return toWatchService.remove(request.params("movieName"), appCache.getProperty("userId"));
            }), new JsonTransformer());
        });
    }

    public void getAllMovies() {
        path("/to-watch/movies", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return toWatchService.getAllMovies(appCache.getProperty("userId"));
            }));
        });
    }

    public void getAllByType() {
        path("/to-watch/by-type", () -> {
            get("/:type", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                return toWatchService.getAllByType(FilmGenre.valueOf(request.params("type")), appCache.getProperty("userId"));
            }));
        });
    }
}
