package bartos.lukasz;

import bartos.lukasz.routing.*;
import bartos.lukasz.runner.App;
import bartos.lukasz.service.ExampleDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import static spark.Spark.port;

@Slf4j
@RequiredArgsConstructor
public class CinemaApp {

    public static void main(String[] args) {
//        startConsoleApp();
        startRoutes();
    }

    public static void startConsoleApp() {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(SuperCinemaAppSpringConfig.class);
        App app = context.getBean("app", App.class);
        app.start();
    }

    public static void startRoutes() {
        // server init
        port(8090);

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(SuperCinemaAppSpringConfig.class);

        context.getBean("exampleDataService", ExampleDataService.class).cleanAndPrepareDatabase();

        context.getBean("userRouting", UserRouting.class).initRoutes();
        context.getBean("cityRouting", CityRouting.class).initRoutes();
        context.getBean("movieRouting", MovieRouting.class).initRoutes();
        context.getBean("seanceRouting", SeanceRouting.class).initRoutes();
        context.getBean("cinemaRoomRouting", CinemaRoomRouting.class).initRoutes();
        context.getBean("cinemaRouting", CinemaRouting.class).initRoutes();
        context.getBean("toWatchRouting", ToWatchRouting.class).initRoutes();
        context.getBean("emailRouting", EmailRouting.class).initRoutes();
        context.getBean("favoriteMoviesRouting", FavoriteMoviesRouting.class).initRoutes();
        context.getBean("movieRatingRouting", MovieRatingRouting.class).initRoutes();
        context.getBean("seatRouting", SeatRouting.class).initRoutes();
        context.getBean("ticketRouting", TicketRouting.class).initRoutes();
        context.getBean("reservationRouting", ReservationRouting.class).initRoutes();
        context.getBean("authorizationRouting", AuthorizationRouting.class).initRoutes();
    }
}