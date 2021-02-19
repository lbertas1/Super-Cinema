package bartos.lukasz.service;

import bartos.lukasz.dto.createModel.*;
import bartos.lukasz.dto.getModel.GetUser;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.exception.JsonConverterException;
import bartos.lukasz.service.jsonConverters.impl.*;
import bartos.lukasz.service.repositoryServices.*;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ExampleDataService {

    private final Jdbi jdbi;

    private final CityService cityService;
    private final CinemaService cinemaService;
    private final CinemaRoomService cinemaRoomService;
    private final MovieService movieService;
    private final SeanceService seanceService;
    private final TicketService ticketService;
    private final UserService userService;
    private final FavoriteMoviesService favoriteMoviesService;
    private final MovieRatingService movieRatingService;
    private final ReservationService reservationService;
    private final ToWatchService toWatchService;
    private final SeatService seatService;

//    String cityFile = "exampleData/exampleCityFile.json";
//    String cinemaFile = "exampleData/exampleCinemaFile.json";
//    String cinemaRoomFile = "exampleData/exampleCinemaRoomFile.json";
//    String movieFile = "exampleData/exampleMovieFile.json";
//    String seanceFile = "exampleData/exampleSeanceFile.json";
//    String userFile = "exampleData/exampleUserFile.json";
//    String ticketFile = "exampleData/exampleTicketFile.json";
//    String movieRatingFile = "exampleData/exampleMovieRatingFile.json";

    public void cleanAndPrepareDatabase() {
        try {
            cleanDatabase();
            prepareNewDatabaseSchema();
            saveExampleDataToDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanDatabase() throws IOException {
        var SQL = "drop table cities, cinemas, cinema_rooms, movies, seances, seats, tickets, movie_ratings, favorite_movieses, to_watches, users, reservations;";
        jdbi.withHandle(handle -> handle.execute(SQL));
    }

    private void prepareNewDatabaseSchema() throws IOException {
        var city = """
                create table if not exists cities (
                                            id integer primary key auto_increment,
                                            name varchar(50) not null
                                        );
                """;

        var cinema = """
                   create table if not exists cinemas (
                                            id integer primary key auto_increment,
                                            name varchar(50) not null,
                                            city_id integer,
                                            foreign key (city_id) references cities(id) on delete cascade on update cascade
                                        );
                """;

        var cinemaRooms = """
                create table if not exists cinema_rooms (
                                            id integer primary key auto_increment,
                                            room_number integer not null,
                                            quantity_of_rows integer not null,
                                            places_in_row integer not null,
                                            cinema_id integer,
                                            foreign key (cinema_id) references cinemas(id) on delete cascade on update cascade
                                        );
                """;

        var movie = """
                create table if not exists movies (
                                            id integer primary key auto_increment,
                                            title varchar(50) not null, 
                                            film_genre varchar(50) not null
                                        );
                """;

        var seances = """
                 create table if not exists seances (
                                            id integer primary key auto_increment,
                                            movie_id integer,
                                            foreign key (movie_id) references movies(id) on delete cascade on update cascade,
                                            screening_date varchar(50) not null,
                                            cinema_room_id integer,
                                            foreign key (cinema_room_id) references cinema_rooms(id) on delete cascade on update cascade
                                        );
                """;

        var seats = """
                create table if not exists seats (
                                            id integer primary key auto_increment,
                                            seat_number integer not null,
                                            seat_row integer not null,
                                            place integer not null,
                                            room_id integer,
                                            foreign key (room_id) references cinema_rooms(id) on delete cascade on update cascade,
                                            seance_id integer, 
                                            foreign key (seance_id) references seances(id) on delete cascade  on update cascade,
                                            seat_status varchar(50) not null 
                                        );
                """;

        var users = """
                create table if not exists users (
                                            id integer primary key auto_increment,
                                            username varchar(100) not null,
                                            password varchar(500) not null,
                                            name varchar(50) not null,
                                            surname varchar(50) not null,
                                            email varchar(50) not null,
                                            age integer not null,
                                            role varchar(50)
                                        );
                """;

        var tickets = """
                create table if not exists tickets (
                                            id integer primary key auto_increment,
                                            price varchar(50) not null,
                                            ticket_type varchar(50) not null
                                        );
                """;

        var movieRating = """
                create table if not exists movie_ratings (
                                            id integer primary key auto_increment,
                                            rate integer not null, 
                                            user_id integer,
                                            foreign key (user_id) references users(id) on delete cascade on update cascade,
                                            movie_id integer,
                                            foreign key (movie_id) references movies(id) on delete cascade on update cascade
                                        );
                """;

        var favoriteMovies = """
                create table if not exists favorite_movieses (
                                            id integer primary key auto_increment, 
                                            user_id integer,
                                            foreign key (user_id) references users(id) on delete cascade on update cascade,
                                            movie_id integer,
                                            foreign key (movie_id) references movies(id) on delete cascade on update cascade
                                        );
                """;

        var moviesToWatch = """
                create table if not exists to_watches (
                                            id integer primary key auto_increment, 
                                            user_id integer,
                                            foreign key (user_id) references users(id) on delete cascade on update cascade,
                                            movie_id integer,
                                            foreign key (movie_id) references movies(id) on delete cascade on update cascade
                                        );
                """;

        var reservations = """
                create table if not exists reservations (
                                            id integer primary key auto_increment, 
                                            city_id integer,
                                            foreign key (city_id) references cities(id) on delete cascade on update cascade,
                                            cinema_id integer,
                                            foreign key (cinema_id) references cinemas(id) on delete cascade on update cascade,
                                            cinema_room_id integer,
                                            foreign key (cinema_room_id) references cinema_rooms(id) on delete cascade on update cascade,                       
                                            movie_id integer,
                                            foreign key (movie_id) references movies(id) on delete cascade on update cascade,                       
                                            seance_id integer,
                                            foreign key (seance_id) references seances(id) on delete cascade on update cascade,                       
                                            seats_id json,                       
                                            ticket_id integer,
                                            foreign key (ticket_id) references tickets(id) on delete cascade on update cascade,                       
                                            user_id integer,
                                            foreign key (user_id) references users(id) on delete cascade on update cascade
                                        );
                """;

        jdbi.useHandle(handle -> {
            handle.execute(city);
            handle.execute(cinema);
            handle.execute(cinemaRooms);
            handle.execute(movie);
            handle.execute(seances);
            handle.execute(seats);
            handle.execute(users);
            handle.execute(tickets);
            handle.execute(movieRating);
            handle.execute(favoriteMovies);
            handle.execute(moviesToWatch);
            handle.execute(reservations);
        });
    }

    public void saveExampleDataToDatabase() {
        List<Long> cityIds = saveCities();
        List<Long> cinemaIds = saveCinemas(cityIds);
        List<Long> cinemaRoomsIds = saveCinemaRooms(cinemaIds);
        List<Long> moviesIds = saveMovies();
        List<Long> seancesIds = saveSeances(cinemaRoomsIds, moviesIds);
        Long userId = saveUser().get();
        List<Long> ticketsId = saveTickets();
        saveMovieRatings(moviesIds, userId);
        saveToWatch(moviesIds.get(0), moviesIds.get(1), userId);
        saveFavoriteMovies(moviesIds.get(0), moviesIds.get(1), userId);

        CreateReservation createReservation1 = CreateReservation
                .builder()
                .cityId(cityIds.get(0))
                .cinemaId(cinemaIds.get(0))
                .cinemaRoomId(cinemaRoomsIds.get(0))
                .movieId(moviesIds.get(0))
                .seanceId(seancesIds.get(0))
                .seatsId(List.of(
                        seatService.getSeatsBySeanceId(seancesIds.get(0)).get(0).getId(),
                        seatService.getSeatsBySeanceId(seancesIds.get(0)).get(1).getId()))
                .ticketId(ticketsId.get(0))
                .userId(userId)
                .build();

        reservationService.save(createReservation1);
    }

    private List<Long> saveCities() {
        CreateCity createCity1 = CreateCity.builder().name("Leicester").build();
        CreateCity createCity2 = CreateCity.builder().name("Manchester").build();
        CreateCity createCity3 = CreateCity.builder().name("Hollywood").build();
        CreateCity createCity4 = CreateCity.builder().name("Chicago").build();
        List<CreateCity> createCities = List.of(createCity1, createCity2, createCity3, createCity4);
        List<Long> cityIds = new ArrayList<>();
        createCities.forEach(createCity -> cityIds.add(cityService.save(createCity).getId()));
        return cityIds;
    }

    private List<Long> saveCinemas(List<Long> cityIds) {
        CreateCinema createCinema1 = CreateCinema.builder().name("CinemaCity").build();
        CreateCinema createCinema2 = CreateCinema.builder().name("Helios").build();
        CreateCinema createCinema3 = CreateCinema.builder().name("SuperCinema").build();
        CreateCinema createCinema4 = CreateCinema.builder().name("MultiCinema").build();
        List<CreateCinema> createCinemas = List.of(createCinema1, createCinema2, createCinema3, createCinema4);
        List<Long> cinemaIds = new ArrayList<>();
        AtomicInteger cityCounter = new AtomicInteger(0);
        createCinemas.forEach(createCinema -> {
            createCinema.setCityId(cityIds.get(cityCounter.get()));
            cityCounter.incrementAndGet();
            cinemaIds.add(cinemaService.save(createCinema).getId());
        });
        return cinemaIds;
    }

    private List<Long> saveCinemaRooms(List<Long> cinemaIds) {
        CreateCinemaRoom createCinemaRoom1 = CreateCinemaRoom.builder().placesInRow(4).quantityOfRows(4).roomNumber(1).build();
        CreateCinemaRoom createCinemaRoom2 = CreateCinemaRoom.builder().placesInRow(5).quantityOfRows(5).roomNumber(2).build();
        CreateCinemaRoom createCinemaRoom3 = CreateCinemaRoom.builder().placesInRow(5).quantityOfRows(5).roomNumber(3).build();
        CreateCinemaRoom createCinemaRoom4 = CreateCinemaRoom.builder().placesInRow(6).quantityOfRows(5).roomNumber(4).build();
        List<CreateCinemaRoom> createCinemaRooms = List.of(createCinemaRoom1, createCinemaRoom2, createCinemaRoom3, createCinemaRoom4);
        List<Long> cinemaRoomIds = new ArrayList<>();
        AtomicInteger cinemaCounter = new AtomicInteger(0);
        createCinemaRooms.forEach(createCinemaRoom -> {
            createCinemaRoom.setCinemaId(cinemaIds.get(cinemaCounter.get()));
            cinemaCounter.incrementAndGet();
            cinemaRoomIds.add(cinemaRoomService.save(createCinemaRoom).getId());
        });

        return cinemaRoomIds;
    }

    private List<Long> saveMovies() {
        CreateMovie createMovie1 = CreateMovie.builder().filmGenre(FilmGenre.HORROR.name()).title("Annabelle").build();
        CreateMovie createMovie2 = CreateMovie.builder().filmGenre(FilmGenre.DRAMA.name()).title("Joker").build();
        CreateMovie createMovie3 = CreateMovie.builder().filmGenre(FilmGenre.ACTION.name()).title("Bad Boys").build();
        CreateMovie createMovie4 = CreateMovie.builder().filmGenre(FilmGenre.COMEDY.name()).title("Intouchables").build();
        CreateMovie createMovie5 = CreateMovie.builder().filmGenre(FilmGenre.SCI_FI.name()).title("Star Wars").build();
        CreateMovie createMovie6 = CreateMovie.builder().filmGenre(FilmGenre.COMEDY.name()).title("Forrest Gump").build();
        CreateMovie createMovie7 = CreateMovie.builder().filmGenre(FilmGenre.THRILLER.name()).title("Fight Club").build();
        CreateMovie createMovie8 = CreateMovie.builder().filmGenre(FilmGenre.HORROR.name()).title("The Shining").build();
        List<CreateMovie> createMovies = List.of(createMovie1, createMovie2, createMovie3, createMovie4, createMovie5, createMovie6, createMovie7, createMovie8);
        List<Long> moviesIds = new ArrayList<>();
        createMovies.forEach(createMovie -> moviesIds.add(movieService.save(createMovie).getId()));
        return moviesIds;
    }

    private List<Long> saveSeances(List<Long> cinemaRoomsIds, List<Long> movieIds) {
        CreateSeance createSeance1 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(1).toString()).build();
        CreateSeance createSeance2 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(2).toString()).build();
        CreateSeance createSeance3 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(3).toString()).build();
        CreateSeance createSeance4 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(4).toString()).build();
        CreateSeance createSeance5 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(1).toString()).build();
        CreateSeance createSeance6 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(2).toString()).build();
        CreateSeance createSeance7 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(3).toString()).build();
        CreateSeance createSeance8 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(4).toString()).build();
        CreateSeance createSeance9 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(1).toString()).build();
        CreateSeance createSeance10 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(2).toString()).build();
        CreateSeance createSeance11 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(3).toString()).build();
        CreateSeance createSeance12 = CreateSeance.builder().screeningDate(LocalDate.now().plusDays(4).toString()).build();
        List<CreateSeance> createSeances = List.of(createSeance1, createSeance2, createSeance3, createSeance4, createSeance5,
                createSeance6, createSeance7, createSeance8, createSeance9, createSeance10, createSeance11, createSeance12);
        List<Long> seancesIds = new ArrayList<>();
        AtomicInteger cinemaRoomsCounter = new AtomicInteger(0);
        AtomicInteger moviesCounter = new AtomicInteger(0);
        createSeances.forEach(createSeance -> {
            if (cinemaRoomsCounter.get() == cinemaRoomsIds.size()) cinemaRoomsCounter.set(0);
            if (moviesCounter.get() == movieIds.size()) moviesCounter.set(0);

            createSeance.setCinemaRoomId(cinemaRoomsIds.get(cinemaRoomsCounter.get()));
            createSeance.setMovieId(movieIds.get(moviesCounter.get()));
            cinemaRoomsCounter.incrementAndGet();
            moviesCounter.incrementAndGet();
            seancesIds.add(seanceService.save(createSeance).getId());
        });
        return seancesIds;
    }

    private AtomicLong saveUser() {
        CreateUser createUser1 = CreateUser.builder().name("Name").surname("Surname").email("tmpEmail@wp.pl").age(30)
                .username("user").password("password").build();
        List<CreateUser> createUsers = List.of(createUser1);
        AtomicLong userId = new AtomicLong();
        createUsers.forEach(createUser -> {
            GetUser save = userService.save(createUser);
            userId.set(save.getId());
        });
        return userId;
    }

    private List<Long> saveTickets() {
        CreateTicket createTicket1 = CreateTicket.builder().price(new BigDecimal("20")).ticketType(TicketType.O).build();
        CreateTicket createTicket2 = CreateTicket.builder().price(new BigDecimal("18")).ticketType(TicketType.O).build();
        CreateTicket createTicket3 = CreateTicket.builder().price(new BigDecimal("16")).ticketType(TicketType.O).build();
        CreateTicket createTicket4 = CreateTicket.builder().price(new BigDecimal("14")).ticketType(TicketType.O).build();
        List<CreateTicket> createTickets = List.of(createTicket1, createTicket2, createTicket3, createTicket4);
        List<Long> ticketsId = new ArrayList<>();
        createTickets.forEach(createTicket -> ticketsId.add(ticketService.save(createTicket).getId()));
        return ticketsId;
    }

    private void saveMovieRatings(List<Long> moviesIds, Long userId) {
        CreateMovieRating createMovieRating1 = CreateMovieRating.builder().rate(4).build();
        CreateMovieRating createMovieRating2 = CreateMovieRating.builder().rate(10).build();
        List<CreateMovieRating> createMovieRatings = List.of(createMovieRating1, createMovieRating2);
        AtomicInteger moviesCounter = new AtomicInteger(0);
        createMovieRatings.forEach(createMovieRating -> {
            if (moviesCounter.get() == moviesIds.size()) moviesCounter.set(0);

            createMovieRating.setUserId(userId);
            createMovieRating.setMovieId(moviesIds.get(moviesCounter.get()));

            moviesCounter.incrementAndGet();

            movieRatingService.save(createMovieRating);
        });
    }

    private void saveToWatch(Long movieId1, Long movieId2, Long userId) {
        CreateToWatch createToWatch1 = CreateToWatch.builder().movieId(movieId1).userId(userId).build();
        CreateToWatch createToWatch2 = CreateToWatch.builder().movieId(movieId2).userId(userId).build();

        toWatchService.save(createToWatch1);
        toWatchService.save(createToWatch2);
    }

    private void saveFavoriteMovies(Long id1, Long id2, Long userId) {
        CreateFavoriteMovies createFavoriteMovies1 = CreateFavoriteMovies.builder().movieId(id1).userId(userId).build();
        CreateFavoriteMovies createFavoriteMovies2 = CreateFavoriteMovies.builder().movieId(id2).userId(userId).build();

        favoriteMoviesService.save(createFavoriteMovies1);
        favoriteMoviesService.save(createFavoriteMovies2);
    }

    private List<CreateCity> readCitiesFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonCityConverter jsonCityConverter = new JsonCityConverter(path);
        return jsonCityConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateCinema> readCinemasFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonCinemaConverter jsonCinemaConverter = new JsonCinemaConverter(path);
        return jsonCinemaConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateCinemaRoom> readCinemaRoomsFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonCinemaRoomConverter jsonCinemaRoomConverter = new JsonCinemaRoomConverter(path);
        return jsonCinemaRoomConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateMovie> readMoviesFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonMovieConverter jsonMovieConverter = new JsonMovieConverter(path);
        return jsonMovieConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateSeance> readSeancesFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonSeanceConverter jsonSeanceConverter = new JsonSeanceConverter(path);
        return jsonSeanceConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateUser> readUsersFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonUserConverter jsonUserConverter = new JsonUserConverter(path);
        return jsonUserConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateTicket> readTicketsFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonTicketConverter jsonTicketConverter = new JsonTicketConverter(path);
        return jsonTicketConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }

    private List<CreateMovieRating> readMovieRatingsFromJson(String path) {
        if (Files.notExists(Paths.get(path))) throw new JsonConverterException("File does not exists");
        JsonMovieRatingConverter jsonMovieRatingConverter = new JsonMovieRatingConverter(path);
        return jsonMovieRatingConverter.fromJson().orElseThrow(() -> new JsonConverterException("Cannot read data from file"));
    }
}
