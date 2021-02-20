package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.enums.Role;
import bartos.lukasz.service.tokens.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static spark.Spark.before;
import static spark.Spark.halt;

@Component
@RequiredArgsConstructor
public class AuthorizationRouting {

    private final TokenService tokenService;
    private final AppCache appCache;

    public void initRoutes() {
        authorization();
    }

    void authorization() {
        before((request, response) -> {

            var uri = request.uri();

            List<String> urisToLogin = List.of(
                    "/users/login",
                    "/users-register");

            if (!urisToLogin.contains(uri) && appCache.getProperty("userId") == null) {
                halt(403, "You should login to using this app");
            }

            if (appCache.getProperty("userId") != null) {
                var user = tokenService.parseAccessToken(request.headers("Authorization"));
                var role = user.getRole();

                if (role.equals(Role.USER)) {
                    List<Pattern> userPatterns = List.of(
                            Pattern.compile("/movies/all"),
                            Pattern.compile("/movies"),
                            Pattern.compile("/cinemas/all"),
                            Pattern.compile("/cinemas/name/\\w+"),
                            Pattern.compile("/movies/watched/\\w+"),
                            Pattern.compile("/movies/by/name/[a-zA-Z0-9. -_]+"),
                            Pattern.compile("/movies/by-type/[A-Z]+"),
                            Pattern.compile("/ratings"),
                            Pattern.compile("/ratings/all"),
                            Pattern.compile("/ratings/user"),
                            Pattern.compile("/ratings/highest-rate"),
                            Pattern.compile("/ratings/highest-average"),
                            Pattern.compile("/ratings/highest-average/type/[a-zA-Z_-]+"),
                            Pattern.compile("/ratings/most-rating"),
                            Pattern.compile("/seats/select/by-number/\\d+"),
                            Pattern.compile("/seats/by-seance/\\d+"),
                            Pattern.compile("/seats/busy/\\d+"),
                            Pattern.compile("/seats/empty"),
                            Pattern.compile("/cinema-rooms/\\w+"),
                            Pattern.compile("/seances/\\d+"),
                            Pattern.compile("/seances/by/movie"),
                            Pattern.compile("/seances/by/date/[0-9-]{10}"),
                            Pattern.compile("/to-watch"),
                            Pattern.compile("/to-watch/remove"),
                            Pattern.compile("/to-watch/movies"),
                            Pattern.compile("/to-watch/by-type/\\w+"),
                            Pattern.compile("/favourite-movies"),
                            Pattern.compile("/favourite-movies/remove/\\w+"),
                            Pattern.compile("/favourite-movies/all"),
                            Pattern.compile("/favourite-movies/by-type/\\w+"),
                            Pattern.compile("/tickets"),
                            Pattern.compile("/tickets/\\d+"),
                            Pattern.compile("/tickets/payment/get"),
                            Pattern.compile("/tickets/payment/pay/\\d+"),
                            Pattern.compile("/email/send/[a-zA-Z0-9._@-]+/[a-zA-Z0-9._@-]+"),
                            Pattern.compile("/cinemas/repertoire/\\w+"),
                            Pattern.compile("/cities/repertoire/\\w+"),
                            Pattern.compile("/cities/all"),
                            Pattern.compile("/users/logout"),
                            Pattern.compile("/reservations/[a-zA-Z0-9_.@]+"),
                            Pattern.compile("/reservations/remove/\\d+")
                    );

                    AtomicInteger counter = new AtomicInteger(0);
                    userPatterns.forEach(pattern -> {
                        if (!pattern.matcher(uri).matches()) {
                            counter.incrementAndGet();
                        }
                    });

                    if (counter.get() == userPatterns.size()) halt(403, "You are not authorized to send this request");
                }
            }
        });
    }
}
