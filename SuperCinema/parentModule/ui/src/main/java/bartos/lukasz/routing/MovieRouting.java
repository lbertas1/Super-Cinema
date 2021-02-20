package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateMovie;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.MovieService;
import bartos.lukasz.service.repositoryServices.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class MovieRouting {

    private final MovieService movieService;
    private final GsonService<CreateMovie> gson;
    private final TicketService ticketService;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        getAll();
        getWatchedMovies();
        findMovieByName();
        getMoviesByType();
    }

    public void save() {
        path("/movies", () -> {
            post("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieService.save(gson.getObjectFromJson(request.body(), CreateMovie.class));
            }), new JsonTransformer());
        });
    }

    public void getAll() {
        path("/movies/all", () -> {
            get("", ((request, response) ->{
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieService.getAllMovies();
            }), new JsonTransformer());
        });
    }

    public void getWatchedMovies() {
        path("/movies/watched", () -> {
            get("/:username", ((request, response) -> {
                String username = request.params("username");
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                return ticketService.getMoviesWatchedByUser(username);
            }), new JsonTransformer());
        });
    }

    public void findMovieByName() {
        path("/movies/by/name", () -> {
            get("/:movieName", ((request, response) -> {
                String movieName = request.params("movieName");
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                GetMovie movieByName = movieService.findMovieByName(movieName);
                appCache.addOrUpdate("movieId", movieByName.getId());
                return movieByName;
            }), new JsonTransformer());
        });
    }

    public void getMoviesByType() {
        path("/movies/by-type", () -> {
            get("/:type", ((request, response) -> {
                String type = request.params("type");
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);
                return movieService.getMoviesByType(type);
            }), new JsonTransformer());
        });
    }
}
