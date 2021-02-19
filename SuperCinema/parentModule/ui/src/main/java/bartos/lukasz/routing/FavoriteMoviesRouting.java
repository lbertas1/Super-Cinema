package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateFavoriteMovies;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.FavoriteMoviesService;
import bartos.lukasz.service.repositoryServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class FavoriteMoviesRouting {

    private final FavoriteMoviesService favoriteMoviesService;
    private final GsonService<CreateFavoriteMovies> gson;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        remove();
        getAllMovies();
        getAllByType();
    }

    public void save() {
        path("/favourite-movies", () -> {
            post("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return favoriteMoviesService.save(gson.getObjectFromJson(request.body(), CreateFavoriteMovies.class));
            }), new JsonTransformer());
        });
    }

    public void remove() {
        path("/favourite-movies/remove", () -> {
            delete("/:movieName", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return favoriteMoviesService.remove(appCache.getProperty("userId"), request.params("movieName"));
            }), new JsonTransformer());
        });
    }

    public void getAllMovies() {
        path("/favourite-movies/all", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return favoriteMoviesService.getAllMovies(appCache.getProperty("userId"));
            }));
        });
    }

    public void getAllByType() {
        path("/favourite-movies/by-type", () -> {
            get("/:type", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return favoriteMoviesService.getAllByType(FilmGenre.valueOf(request.params("type")), appCache.getProperty("userId"));
            }));
        });
    }
}
