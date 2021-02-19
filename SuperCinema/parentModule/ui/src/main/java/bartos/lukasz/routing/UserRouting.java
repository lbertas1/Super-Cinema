package bartos.lukasz.routing;

import bartos.lukasz.cache.AppCache;
import bartos.lukasz.dto.UserPrincipal;
import bartos.lukasz.dto.createModel.CreateUser;
import bartos.lukasz.dto.getModel.GetUser;
import bartos.lukasz.service.jsonConverters.GsonService;
import bartos.lukasz.service.jsonConverters.JsonTransformer;
import bartos.lukasz.service.repositoryServices.UserService;
import bartos.lukasz.service.tokens.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRouting {

    private final UserService userService;
    private final GsonService<CreateUser> createUserGson;
    private final GsonService<UserPrincipal> userPrincipalGson;
    private final TokenService tokenService;
    private final AppCache appCache;

    public void initRoutes() {
        registration();
        login();
        logout();
    }

    public void registration() {
        path("/users-register", () -> {
            post("", ((request, response) -> {
                var newUser = createUserGson.getObjectFromJson(request.body(), CreateUser.class);

                if (!checkUsernameIdDatabase(newUser.getUsername())) {
                    GetUser getUser = userService.save(newUser);
                    response.header("Content-Type", "application/json;charset=utf-8");
                    response.status(201);
                    return getUser.getUsername() + " registered correctly.";
                } else {
                    response.status(409);
                    return "Given username isn't empty";
                }
            }));
        });
    }

    public void login() {
        path("/users/login", () -> {
            post("", (request, response) -> {

                UserPrincipal userPrincipal = userPrincipalGson.getObjectFromJson(request.body(), UserPrincipal.class);
                GetUser getUser = userService.isPasswordValid(userPrincipal.getUsername(), userPrincipal.getPassword());
                appCache.addOrUpdate("userId", getUser.getId());
                var tokens = tokenService.generateTokens(getUser);
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return tokens;
            }, new JsonTransformer());
        });
    }

    public void logout() {
        path("/users/logout", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);

                appCache.clearCache();
                return "Logout";
            }));
        });
    }

    private boolean checkUsernameIdDatabase(String username) {
        return userService.checkUsernameInDatabase(username);
    }
}
