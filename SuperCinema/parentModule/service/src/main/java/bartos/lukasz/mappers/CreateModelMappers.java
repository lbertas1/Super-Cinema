package bartos.lukasz.mappers;

import bartos.lukasz.dto.createModel.*;
import bartos.lukasz.enums.Role;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.exception.ValidationException;
import bartos.lukasz.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public interface CreateModelMappers {

    static City toCity(CreateCity createCity) {
        if (Objects.isNull(createCity)) throw new ValidationException("Object cannot be null");

        return City
                .builder()
                .name(toUpperLetter(createCity.getName()))
                .build();
    }

    static Cinema toCinema(CreateCinema createCinema) {
        if (Objects.isNull(createCinema)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createCinema.getCityId())) throw new ValidationException("Field city id is null");

        return Cinema
                .builder()
                .cityId(createCinema.getCityId())
                .name(toUpperLetter(createCinema.getName()))
                .build();
    }

    static CinemaRoom toCinemaRoom(CreateCinemaRoom createCinemaRoom) {
        if (Objects.isNull(createCinemaRoom)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createCinemaRoom.getCinemaId())) throw new ValidationException("Field cinema id is null");
        if (Objects.isNull(createCinemaRoom.getRoomNumber())) throw new ValidationException("Field room number is null");
        if (Objects.isNull(createCinemaRoom.getPlacesInRow())) throw new ValidationException("Field places in row is null");
        if (Objects.isNull(createCinemaRoom.getQuantityOfRows())) throw new ValidationException("Field quantity of row is null");

        return CinemaRoom
                .builder()
                .cinemaId(createCinemaRoom.getCinemaId())
                .placesInRow(createCinemaRoom.getPlacesInRow())
                .quantityOfRows(createCinemaRoom.getQuantityOfRows())
                .roomNumber(createCinemaRoom.getRoomNumber())
                .build();
    }

    static Movie toMovie(CreateMovie createMovie) {
        if (Objects.isNull(createMovie)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createMovie.getFilmGenre())) throw new ValidationException("Field film genre cannot be null");
        if (Objects.isNull(createMovie.getTitle())) throw new ValidationException("Field title cannot be null");

        return Movie
                .builder()
                .filmGenre(createMovie.getFilmGenre())
                .title(toUpperLetter(createMovie.getTitle()))
                .build();
    }

    static Seance toSeance(CreateSeance createSeance) {
        if (Objects.isNull(createSeance)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createSeance.getCinemaRoomId())) throw new ValidationException("Field cinema room id cannot be null");
        if (Objects.isNull(createSeance.getMovieId())) throw new ValidationException("Field movie id cannot be null");
        if (Objects.isNull(createSeance.getScreeningDate())) throw new ValidationException("Field screening date cannot be null");

        return Seance
                .builder()
                .screeningDate(LocalDate.parse(createSeance.getScreeningDate()))
                .cinemaRoomId(createSeance.getCinemaRoomId())
                .movieId(createSeance.getMovieId())
                .build();
    }

    static Seat toSeat(CreateSeat createSeat) {
        if (Objects.isNull(createSeat)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createSeat.getPlace())) throw new ValidationException("Field place cannot be null");
        if (Objects.isNull(createSeat.getRoomId())) throw new ValidationException("Field room id cannot be null");
        if (Objects.isNull(createSeat.getSeanceId())) throw new ValidationException("Field seance id cannot be null");
        if (Objects.isNull(createSeat.getSeatNumber())) throw new ValidationException("Field seat number cannot be null");
        if (Objects.isNull(createSeat.getSeatRow())) throw new ValidationException("Field seat row cannot be null");

        return Seat
                .builder()
                .seatNumber(createSeat.getSeatNumber())
                .place(createSeat.getPlace())
                .seatRow(createSeat.getSeatRow())
                .roomId(createSeat.getRoomId())
                .seanceId(createSeat.getSeanceId())
                .seatStatus(SeatStatus.EMPTY.toString())
                .build();
    }

    static Ticket toTicket(CreateTicket createTicket) {
        if (Objects.isNull(createTicket)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createTicket.getPrice())) {
            createTicket.setPrice(new BigDecimal(""));
        }
        if (Objects.isNull(createTicket.getTicketType())) throw new ValidationException("Field ticket type cannot be null");

        return Ticket
                .builder()
                .price(createTicket.getPrice())
                .ticketType(createTicket.getTicketType().toString())
                .build();
    }

    static User toUser(CreateUser createUser) {
        if (Objects.isNull(createUser)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createUser.getUsername())) throw new ValidationException("Field username cannot be null");
        if (Objects.isNull(createUser.getPassword())) throw new ValidationException("Field password cannot be null");
        if (Objects.isNull(createUser.getName())) throw new ValidationException("Field name cannot be null");
        if (Objects.isNull(createUser.getSurname())) throw new ValidationException("Field surname cannot be null");
        if (Objects.isNull(createUser.getAge())) throw new ValidationException("Field age cannot be null");
        if (Objects.isNull(createUser.getEmail())) throw new ValidationException("Field email cannot be null");

        return User
                .builder()
                .username(createUser.getUsername())
                .password(createUser.getPassword())
                .name(toUpperLetter(createUser.getName()))
                .surname(toUpperLetter(createUser.getSurname()))
                .age(createUser.getAge())
                .email(createUser.getEmail())
                .role(Role.USER.toString())
                .build();
    }

    static MovieRating toMovieRating(CreateMovieRating createMovieRating) {
        if (Objects.isNull(createMovieRating)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createMovieRating.getMovieId())) throw new ValidationException("Field movie id cannot be null");
        if (Objects.isNull(createMovieRating.getRate()) || createMovieRating.getRate() < 0 || createMovieRating.getRate() > 10) throw new ValidationException("Incorrect field rate");
        if (Objects.isNull(createMovieRating.getUserId())) throw new ValidationException("Field user id cannot be null");

        return MovieRating
                .builder()
                .rate(createMovieRating.getRate())
                .userId(createMovieRating.getUserId())
                .movieId(createMovieRating.getMovieId())
                .build();
    }

    static ToWatch toWatch(CreateToWatch createToWatch) {
        if (Objects.isNull(createToWatch)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createToWatch.getUserId())) throw new ValidationException("Field user id cannot be null");
        if (Objects.isNull(createToWatch.getMovieId())) throw new ValidationException("Field movie id cannot be null");

        return ToWatch
                .builder()
                .movieId(createToWatch.getMovieId())
                .userId(createToWatch.getUserId())
                .build();
    }

    static FavoriteMovies toFavoriteMovies(CreateFavoriteMovies createFavoriteMovies) {
        if (Objects.isNull(createFavoriteMovies)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createFavoriteMovies.getMovieId())) throw new ValidationException("Field movie id cannot be null");
        if (Objects.isNull(createFavoriteMovies.getUserId())) throw new ValidationException("Field user id cannot be null");

        return FavoriteMovies
                .builder()
                .movieId(createFavoriteMovies.getMovieId())
                .userId(createFavoriteMovies.getUserId())
                .build();
    }

    static Reservation toReservation(CreateReservation createReservation) {
        if (Objects.isNull(createReservation)) throw new ValidationException("Object cannot be null");
        if (Objects.isNull(createReservation.getCityId())) throw new ValidationException("Field city id cannot be null");
        if (Objects.isNull(createReservation.getCinemaId())) throw new ValidationException("Field cinema id cannot be null");
        if (Objects.isNull(createReservation.getCinemaRoomId())) throw new ValidationException("Field cinema room id cannot be null");
        if (Objects.isNull(createReservation.getMovieId())) throw new ValidationException("Field movie id cannot be null");
        if (Objects.isNull(createReservation.getSeanceId())) throw new ValidationException("Field seance id cannot be null");
        if (Objects.isNull(createReservation.getSeatsId())) throw new ValidationException("Field seats id list cannot be null");
        if (Objects.isNull(createReservation.getTicketId())) throw new ValidationException("Field ticket id cannot be null");
        if (Objects.isNull(createReservation.getUserId())) throw new ValidationException("Field user id cannot be null");

        return Reservation
                .builder()
                .cityId(createReservation.getCityId())
                .cinemaId(createReservation.getCinemaId())
                .cinemaRoomId(createReservation.getCinemaRoomId())
                .movieId(createReservation.getMovieId())
                .seanceId(createReservation.getSeanceId())
                .seatsId(createReservation.getSeatsId())
                .ticketId(createReservation.getTicketId())
                .userId(createReservation.getUserId())
                .build();
    }

    private static String toUpperLetter(String word) {
        char firstLetter = word.charAt(0);
        if (Character.isLowerCase(firstLetter)) word = word.replace(firstLetter, Character.toUpperCase(firstLetter));

        return word;
    }
}
