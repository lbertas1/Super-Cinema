package bartos.lukasz.routing;

import bartos.lukasz.dto.createModel.CreateReservation;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class ReservationRouting {

    private final ReservationService reservationService;
    private final GsonService<CreateReservation> gson;

    public void initRoutes() {
        save();
        getUserReservations();
        cancelReservation();
    }

    public void save() {
        path("/reservations", () -> {
            post("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return reservationService.save(gson.getObjectFromJson(request.body(), CreateReservation.class));
            }), new JsonTransformer());
        });
    }

    public void getUserReservations() {
        path("/reservations", () -> {
            get("/:login", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return reservationService.getAllUserReservationByUsername(request.params("login"));
            }), new JsonTransformer());
        });
    }

    public void cancelReservation() {
        path("/reservations/remove", () -> {
            delete("/:id", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return reservationService.remove(Long.valueOf(request.params("id")));
            }), new JsonTransformer());
        });
    }
}
