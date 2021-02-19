package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.createModel.CreateMovieRating;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.RoutingException;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.MovieRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class MovieRatingRouting {

    private final MovieRatingService movieRatingService;
    private final GsonService<CreateMovieRating> gson;
    private final AppCache appCache;

    public void initRoutes() {
        save();
        getAll();
        getAllUserRatings();
        getUserHighestRate();
        getHighestAverage();
        getHighestAverageInType();
        getMostRatedMovie();
    }

    public void save() {
        path("/ratings", () -> {
            post("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(201);

                CreateMovieRating createMovieRating = gson.getObjectFromJson(request.body(), CreateMovieRating.class);
                if (createMovieRating.getRate() > 10) throw new RoutingException("Too high grade");

                return movieRatingService.save(createMovieRating);
            }), new JsonTransformer());
        });
    }

    public void getAll() {
        path("/ratings/all", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieRatingService.getAll();
            }));
        });
    }

    public void getAllUserRatings() {
        path("/ratings/user", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieRatingService.getAllUserRatings(appCache.getProperty("userId"));
            }));
        });
    }

    public void getUserHighestRate() {
        path("/ratings/highest-rate", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieRatingService.getMovieWithHighestRateByUser(appCache.getProperty("userId"));
            }));
        });
    }

    public void getHighestAverage() {
        path("/ratings/highest-average", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieRatingService.getMovieWithHighestAvgRate();
            }));
        });
    }

    public void getHighestAverageInType() {
        path("/ratings/highest-average/type", () -> {
            get("/:type", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieRatingService.getMovieWithHighestAvgRateInCategory(FilmGenre.valueOf(request.params("type")));
            }));
        });
    }

    public void getMostRatedMovie() {
        path("/ratings/most-rating", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return movieRatingService.getMostOftenRatingMovie();
            }));
        });
    }
}
