package bartos.lukasz.runner;

import bartos.lukasz.dto.createModel.CreateFavoriteMovies;
import bartos.lukasz.dto.createModel.CreateMovieRating;
import bartos.lukasz.dto.createModel.CreateTicket;
import bartos.lukasz.dto.createModel.CreateToWatch;
import bartos.lukasz.dto.getModel.*;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.exception.UserDataException;
import bartos.lukasz.service.UserData;
import bartos.lukasz.service.emailServices.EmailService;
import bartos.lukasz.service.emailServices.PdfFileService;
import bartos.lukasz.service.repositoryServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class App {

    private final MovieService movieService;
    private final SeanceService seanceService;
    private final UserService userService;
    private final CityService cityService;
    private final CinemaService cinemaService;
    private final CinemaRoomService cinemaRoomService;
    private final SeatService seatService;
    private final TicketService ticketService;
    private final MovieRatingService movieRatingService;
    private final FavoriteMoviesService favoriteMoviesService;
    private final ToWatchService toWatchService;

    private GetUser getUser;
    private boolean session;

    public boolean start() {
        System.out.println("Welcome on SuperHyperCinema website!");

        while (!session) {
            if (Objects.isNull(getUser)) menuBeforeLoginIn();
            else menuAfterLoginIn();
        }

        return session;
    }

    private int chooseOptionBeforeLogin() {
        System.out.println("0. Close panel.");
        System.out.println("1. Login, or create your account.");
        System.out.println("2. Show all movies.");
        System.out.println("3. Show all cinemas.");
        System.out.println("4. Show all cinemas from city.");
        System.out.println("5. Show cinema rooms from cinema.");
        System.out.println("6. Show movie by name.");
        System.out.println("7. Show movies by type.");
        System.out.println("8. Show seances by date.");
        System.out.println("9. Show seances by movie.");
        return UserData.getInt("Choose action: ");
    }

    private int chooseOptionAfterLogin() {
        System.out.println("0. Logout.");
        System.out.println("1. Show all movies.");
        System.out.println("2. Show all cinemas.");
        System.out.println("3. Show all cinemas from city.");
        System.out.println("4. Show cinema rooms from cinema.");
        System.out.println("5. Show movie by name.");
        System.out.println("6. Show movies by type.");
        System.out.println("7. Show seances by date.");
        System.out.println("8. Show seances by movie.");
        System.out.println("9. Search movie by name.");
        System.out.println("10. Search movies by film genre.");
        System.out.println("11. Search seances by date.");
        System.out.println("12. Search repertoire from chosen cinema.");
        System.out.println("13. Search repertoire from chosen city.");
        System.out.println("14. Add rate to movie.");
        System.out.println("15. Add movie to favorite movies list");
        System.out.println("16. Add movie to to watch list");
        System.out.println("17. Get all your ratings");
        System.out.println("18. Get movie with your highest rate");
        System.out.println("19. Get movie with highest average rate");
        System.out.println("20. Get movie with highest average rate in category");
        System.out.println("21. Get most often rating movie");
        System.out.println("22. Get all movies from your favorite list");
        System.out.println("23. Get all movies from your favorite list by type");
        System.out.println("24. Get all movies from your to watch list");
        System.out.println("25. Get all movies from your to watch list by type");
        return UserData.getInt("Choose action: ");
    }

    private void menuBeforeLoginIn() {
        int option = 0;

        do {
            option = chooseOptionBeforeLogin();
            switch (option) {
                case 0 -> {
                    session = true;
                    return;
                }
                case 1 -> loginIn();
                case 2 -> System.out.println(movieService.getAllMovies());
                case 3 -> System.out.println(cinemaService.getAllCinemas());
                case 4 -> System.out.println(cinemaService.getCinemaFromCity(UserData.getString("Enter city name")));
                case 5 -> System.out.println(cinemaRoomService.getCinemaRoomsByCinemaName(UserData.getString("Enter cinema name")));
                case 6 -> System.out.println(movieService.findMovieByName(UserData.getString("Enter movie name")));
                case 7 -> System.out.println(movieService.getMoviesByType(UserData.getString("Enter movie type")));
                case 8 -> System.out.println(seanceService.findSeancesByGivenDate(UserData.getDate("Enter date").toString()));
                case 9 -> System.out.println(seanceService.getSeancesByFilmName(UserData.getString("Enter movie name")));
                default -> System.out.println("No such option");
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (getUser == null);
    }

    private void menuAfterLoginIn() {
        int option = 0;

        do {
            option = chooseOptionAfterLogin();
            switch (option) {
                case 0 -> {
                    logout();
                    session = true;
                    return;
                }
                case 1 -> System.out.println(movieService.getAllMovies());
                case 2 -> System.out.println(cinemaService.getAllCinemas());
                case 3 -> System.out.println(cinemaService.getCinemaFromCity(UserData.getString("Enter city name")));
                case 4 -> System.out.println(cinemaRoomService.getCinemaRoomsByCinemaName(UserData.getString("Enter cinema name")));
                case 5 -> System.out.println(movieService.findMovieByName(UserData.getString("Enter movie name")));
                case 6 -> System.out.println(movieService.getMoviesByType(UserData.getString("Enter movie type")));
                case 7 -> System.out.println(seanceService.findSeancesByGivenDate(UserData.getDate("Enter date").toString()));
                case 8 -> System.out.println(seanceService.getSeancesByFilmName(UserData.getString("Enter movie name")));
                case 9 -> searchMovieByName();
                case 10 -> searchMovieByFilmGenre();
                case 11 -> searchSeancesByDate();
                case 12 -> searchRepertoireFromChosenCinema();
                case 13 -> searchRepertoireFromChosenCity();
                case 14 -> addRateToMovie();
                case 15 -> addMovieToFavoriteList();
                case 16 -> addMovieToWatchList();
                case 17 -> getAllUserRatings();
                case 18 -> getMovieWithHighestRateByUser();
                case 19 -> getMovieWithHighestAvgRate();
                case 20 -> getMovieWithHighestAvgRateInCategory();
                case 21 -> getMostOftenRatingMovie();
                case 22 -> getAllMoviesFromUserFavoriteList();
                case 23 -> getAllMoviesFromUserFavoriteListByType();
                case 24 -> getAllMoviesFromUserToWatchList();
                case 25 -> getAllMoviesFromUserToWatchListByType();
                default -> System.out.println("No such option");
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (option != 0);
    }

    private void searchMovieByName() {
        GetMovie movieDto = movieService.findMovieByName(UserData.getString("Enter movie name"));
        List<GetSeance> seancesByFilmName = seanceService.getSeancesByFilmName(movieDto.getTitle());
        seanceService.showSeances(seancesByFilmName);
        if (UserData.getBoolean("Are you interested one of presented terms?")) {
            seanceService.chooseSeance(seancesByFilmName);
            GetSeance seanceDto = seancesByFilmName.get(UserData.getInt("Provide chosen option:") - 1);
            buyTicket(seanceDto, movieDto, null);
        }
    }

    private void searchMovieByFilmGenre() {
        List<GetMovie> moviesByType = movieService.getMoviesByType(UserData.getString("Enter the genre of the movie you are interested in"));
        movieService.showMovies(moviesByType);
        GetMovie movieDto = moviesByType.get(UserData.getInt("Enter number of film you want to choose") - 1);
        List<GetSeance> seancesByFilmName = seanceService.getSeancesByFilmName(movieDto.getTitle());
        seanceService.showSeances(seancesByFilmName);
        if (UserData.getBoolean("Are you interested one of presented terms?")) {
            seanceService.chooseSeance(seancesByFilmName);
            GetSeance seanceDto = seancesByFilmName.get(UserData.getInt("Provide chosen option:") - 1);
            buyTicket(seanceDto, movieDto, null);
        }
    }

    private void searchSeancesByDate() {
        List<GetSeance> seancesByDate = seanceService.findSeancesByGivenDate(UserData.getString("Provide date in the following format: yyyy-MM-dd"));
        if (seancesByDate.isEmpty())
            throw new ServiceException("Sorry, no seances were found in given period");
        List<GetMovie> moviesByListId = movieService.getAllByMoviesIdList(seancesByDate);
        movieService.showMovies(moviesByListId);
        GetMovie movieDto = moviesByListId.get(UserData.getInt("Enter number of film you want to choose") - 1);
        GetSeance seanceDto = seancesByDate
                .stream()
                .filter(seance -> seance.getMovieId().equals(movieDto.getId()))
                .findFirst()
                .orElseThrow(() -> new ServiceException("Movie not found"));
        buyTicket(seanceDto, movieDto, null);
    }

    private void searchRepertoireFromChosenCinema() {
        GetCinema cinemaDto = cinemaService.getCinemaByName(UserData.getString("Provide cinema name"));
        GetCity cityDto = cityService.findById(cinemaDto.getCityId());
        List<GetCinemaRoom> cinemaRoomsByCinemaId = cinemaRoomService.getCinemaRoomsByCinemaId(cinemaDto);
        System.out.println("cinemaRoomsByCinemaId = " + cinemaRoomsByCinemaId);
        GetMovie movieDto = getMovieFromCinemaRoomsList(cinemaRoomsByCinemaId);
        List<GetSeance> seancesByFilmName = seanceService.getSeancesByFilmName(movieDto.getTitle());
        seanceService.showSeances(seancesByFilmName);
        if (UserData.getBoolean("Are you interested one of presented terms?")) {
            seanceService.chooseSeance(seancesByFilmName);
            GetSeance seanceDto = seancesByFilmName.get(UserData.getInt("Provide chosen option:") - 1);
            buyTicket(seanceDto, movieDto, cityDto);
        }
    }

    private void searchRepertoireFromChosenCity() {
        GetCity cityDto = cityService.getCityByName(UserData.getString("Enter the name of the city where you are looking for the movie"));
        List<GetCinema> cinemaFromCity = cinemaService.getCinemaFromCity(cityDto.getName());
        List<GetCinemaRoom> cinemaRoomsFromCinemaList = cinemaRoomService.getCinemaRoomsFromCinemaList(cinemaFromCity);
        GetMovie movieDto = getMovieFromCinemaRoomsList(cinemaRoomsFromCinemaList);
        List<GetSeance> seancesByFilmName = seanceService.getSeancesByFilmName(movieDto.getTitle());

        Map<GetSeance, GetCinema> seanceCinemaMap = seancesByFilmName
                .stream()
                .collect(Collectors.toMap(
                        seanceDto -> seanceDto,
                        seanceDto -> cinemaService.findCinemaById(cinemaRoomService.findById(seanceDto.getCinemaRoomId()).getCinemaId())
                ));

        AtomicInteger counter = new AtomicInteger(1);
        seanceCinemaMap.forEach((seanceDto, cinemaDto) -> {
            System.out.println(counter.getAndIncrement() + " - " + "seance date: " + seanceDto.getScreeningDate() + " in cinema: " + cinemaDto.getName());
        });

        GetSeance seanceDto = seancesByFilmName.get(UserData.getInt("Enter number of seance, you chose") - 1);
        buyTicket(seanceDto, movieDto, cityDto);
    }

    private GetMovie getMovieFromCinemaRoomsList(List<GetCinemaRoom> cinemaRoomsByCinemaId) {
        List<GetSeance> repertoireFromCinema = seanceService.getSeancesByCinemaRoomsId(cinemaRoomsByCinemaId);
        System.out.println("repertoireFromCinema = " + repertoireFromCinema);
        List<GetMovie> allMoviesByIdList = movieService.getAllByMoviesIdList(repertoireFromCinema);
        movieService.showMovies(allMoviesByIdList);
        return allMoviesByIdList.get(UserData.getInt("Enter number of film you want to choose") - 1);
    }

    private void buyTicket(GetSeance seanceDto, GetMovie movieDto, GetCity cityDto) {
        int quantityOfTickets = UserData.getInt("How many ticket you want to buy?");
        for (int i = 0; i < quantityOfTickets; i++) {
            GetUser userDto = loginIn();
            GetCinemaRoom cinemaRoomDto = cinemaRoomService.findById(seanceDto.getCinemaRoomId());
            seatService.showSeats(SeatStatus.EMPTY, seanceDto.getId());
            GetSeat getSeat = seatService.getSeatByNumber(seanceDto.getId(), UserData.getInt("Enter seat number you choose"));
            BigDecimal price = new BigDecimal(String.valueOf(new Random().nextInt(20) + 8));
            GetTicket ticketDto = createTicket(userDto.getId(), getSeat.getId(), seanceDto.getId(), getTicketType(price), price);
            GetCinema cinemaDto = cinemaService.findCinemaById(cinemaRoomDto.getCinemaId());
            if (cityDto == null) cityDto = cityService.findById(cinemaDto.getCityId());
            sendEmailWithTicketPdf(ticketDto, userDto, List.of(getSeat), movieDto, seanceDto, cityDto, cinemaDto, cinemaRoomDto);
        }
    }

    private TicketType getTicketType(BigDecimal price) {
        boolean answer = UserData.getBoolean("Enter y if you want to buy ticket now. " +
                "If you don't want to pay now enter no and your ticket will be reserved");

        if (answer) {
            System.out.println("The ticket price is " + price);
            if (!new BigDecimal(UserData.getString("Enter amount to pay for ticket")).equals(price)) {
                throw new UserDataException("Incorrect amount entered. The seat has not been booked");
            }
            return TicketType.O;
        } else return TicketType.R;
    }

    private GetTicket createTicket(Long userId, Long seatId, Long seanceId, TicketType ticketType, BigDecimal price) {
        CreateTicket createTicket = CreateTicket
                .builder()
                .price(price)
                .ticketType(ticketType)
                .build();

        return ticketService.save(createTicket);
    }

    private GetUser loginIn() {
        if (Objects.isNull(getUser)) {
            if (UserData.getBoolean("Do you have an account?")) {
                String username = UserData.getString("Provide username");
                String password = UserData.getString("Provide password");
                getUser = userService.getUserByUsernameAndPassword(username, password);
                return getUser;
            } else {
                System.out.println("Create your account:");
                getUser = userService.createUser(
                        UserData.getString("Provide username"),
                        UserData.getString("Provide password"),
                        UserData.getString("Provide name"),
                        UserData.getString("Provide surname"),
                        UserData.getString("Provide email"),
                        UserData.getInt("Provide your age")
                );
                return getUser;
            }
        } else return getUser;
    }

    private void sendEmailWithTicketPdf(GetTicket ticketDto, GetUser userDto, List<GetSeat> seats, GetMovie movieDto, GetSeance seanceDto,
                                        GetCity cityDto, GetCinema cinemaDto, GetCinemaRoom cinemaRoomDto) {
        String emailAddress = UserData.getString("Provide your email address to send message");
        String emailPassword = UserData.getString("Provide your email password to send message");
        PdfFileService.createTicket(ticketDto, userDto, seats, movieDto, seanceDto, cityDto, cinemaDto, cinemaRoomDto);
        EmailService.send(userDto.getEmail(), emailAddress, emailPassword);
    }

    private void addRateToMovie() {
        GetUser user = getUser();
        GetMovie movie = getMovieByName();
        CreateMovieRating createMovieRating = CreateMovieRating
                .builder()
                .rate(UserData.getInt("Provide your rate"))
                .movieId(movie.getId())
                .userId(user.getId())
                .build();

        GetMovieRating getMovieRating = movieRatingService.save(createMovieRating);

        if (Objects.nonNull(getMovieRating)) System.out.println("Thank you for your rate. Please come back.");
        else System.out.println("An unidentified problem has occurred. The operation failed");
    }

    private GetMovie getMovieByName() {
        String movieName = UserData.getString("Provide movie name");
        return movieService.findMovieByName(movieName);
    }

    private void addMovieToFavoriteList() {
        GetUser user = getUser;
        GetMovie movie = getMovieByName();
        CreateFavoriteMovies createFavoriteMovies = CreateFavoriteMovies
                .builder()
                .movieId(movie.getId())
                .userId(user.getId())
                .build();

        GetFavoriteMovies favoriteMovie = favoriteMoviesService.save(createFavoriteMovies);

        if (Objects.nonNull(favoriteMovie)) System.out.println("Movie added to list");
        else System.out.println("An unidentified problem has occurred. The operation failed");
    }

    private void addMovieToWatchList() {
        GetUser user = getUser;
        GetMovie movie = getMovieByName();
        CreateToWatch createToWatch = CreateToWatch
                .builder()
                .movieId(movie.getId())
                .userId(user.getId())
                .build();

        GetToWatch getToWatch = toWatchService.save(createToWatch);

        if (Objects.nonNull(getToWatch)) System.out.println("Movie added to list");
        else System.out.println("An unidentified problem has occurred. The operation failed");
    }

    private void getAllUserRatings() {
        if (Objects.isNull(getUser)) {
            System.out.println("To rate a ticket, you must login");
            getUser = loginIn();
        }

        System.out.println("all user ratings: " + movieRatingService.getAllUserRatings(getUser.getId()));
    }

    private GetUser getUser() {
        if (Objects.isNull(getUser)) {
            System.out.println("To rate a ticket, you must login");
            getUser = loginIn();
        }
        return getUser;
    }

    private void getMovieWithHighestRateByUser() {
        System.out.println("Movie with highest rate = " + movieRatingService.getMovieWithHighestRateByUser(getUser.getId()));
    }

    private void getMovieWithHighestAvgRate() {
        System.out.println("Movie with highest rate = " + movieRatingService.getMovieWithHighestAvgRate());
    }

    private void getMovieWithHighestAvgRateInCategory() {
        System.out.println("Movie with highest rate in category = " + movieRatingService.getMovieWithHighestAvgRateInCategory(UserData.getFilmGenre()));
    }

    private void getMostOftenRatingMovie() {
        System.out.println("Movie most often rated = " + movieRatingService.getMostOftenRatingMovie());
    }

    private void getAllMoviesFromUserFavoriteList() {
        System.out.println("Movies from user favorite movies list: " + favoriteMoviesService.getAllMovies(getUser.getId()));
    }

    private void getAllMoviesFromUserFavoriteListByType() {
        System.out.println("Movies from user favorite movies list by type: " + favoriteMoviesService.getAllByType(UserData.getFilmGenre(), getUser.getId()));
    }

    private void getAllMoviesFromUserToWatchList() {
        System.out.println("Movies from user to watch list: " + toWatchService.getAllMovies(getUser.getId()));
    }

    private void getAllMoviesFromUserToWatchListByType() {
        System.out.println("Movies from user to watch list by type: " + toWatchService.getAllByType(UserData.getFilmGenre(), getUser.getId()));
    }

    private void logout() {
        getUser = null;
        session = true;
        System.out.println("Logout successfully");
    }
}
