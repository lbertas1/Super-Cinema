package bartos.lukasz.mappers;

import bartos.lukasz.dto.getModel.*;
import bartos.lukasz.enums.Role;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.model.*;

public interface GetModelMappers {

    static City toCity(GetCity getCity) {
        return City
                .builder()
                .id(getCity.getId())
                .name(getCity.getName())
                .build();
    }

    static GetCity toGetCity(City city) {
        return GetCity
                .builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }

    static Cinema toCinema (GetCinema getCinema) {
        return Cinema
                .builder()
                .id(getCinema.getId())
                .name(getCinema.getName())
                .cityId(getCinema.getCityId())
                .build();
    }

    static GetCinema toGetCinema (Cinema cinema) {
        return GetCinema
                .builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .cityId(cinema.getCityId())
                .build();
    }

    static CinemaRoom toCinemaRoom (GetCinemaRoom getCinemaRoom) {
        return CinemaRoom
                .builder()
                .id(getCinemaRoom.getId())
                .cinemaId(getCinemaRoom.getCinemaId())
                .placesInRow(getCinemaRoom.getPlacesInRow())
                .quantityOfRows(getCinemaRoom.getQuantityOfRows())
                .roomNumber(getCinemaRoom.getRoomNumber())
                .build();
    }

    static GetCinemaRoom toGetCinemaRoom (CinemaRoom cinemaRoom) {
        return GetCinemaRoom
                .builder()
                .id(cinemaRoom.getId())
                .cinemaId(cinemaRoom.getCinemaId())
                .placesInRow(cinemaRoom.getPlacesInRow())
                .quantityOfRows(cinemaRoom.getQuantityOfRows())
                .roomNumber(cinemaRoom.getRoomNumber())
                .build();
    }

    static Movie toMovie(GetMovie getMovie) {
        return Movie
                .builder()
                .id(getMovie.getId())
                .filmGenre(getMovie.getFilmGenre())
                .title(getMovie.getTitle())
                .build();
    }

    static GetMovie toGetMovie(Movie movie) {
        return GetMovie
                .builder()
                .id(movie.getId())
                .filmGenre(movie.getFilmGenre())
                .title(movie.getTitle())
                .build();
    }

    static Seance toSeance(GetSeance getSeance) {
        return Seance
                .builder()
                .id(getSeance.getId())
                .movieId(getSeance.getMovieId())
                .cinemaRoomId(getSeance.getCinemaRoomId())
                .screeningDate(getSeance.getScreeningDate())
                .build();
    }

    static GetSeance toGetSeance(Seance seance) {
        return GetSeance
                .builder()
                .id(seance.getId())
                .movieId(seance.getMovieId())
                .cinemaRoomId(seance.getCinemaRoomId())
                .screeningDate(seance.getScreeningDate())
                .build();
    }

    static Seat toSeat(GetSeat getSeat) {
        return Seat
                .builder()
                .id(getSeat.getId())
                .seatNumber(getSeat.getSeatNumber())
                .roomId(getSeat.getRoomId())
                .seanceId(getSeat.getSeanceId())
                .place(getSeat.getPlace())
                .seatRow(getSeat.getSeatRow())
                .seatStatus(getSeat.getSeatStatus().toString())
                .build();
    }

    static GetSeat toGetSeat(Seat seat) {
        return GetSeat
                .builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .roomId(seat.getRoomId())
                .seanceId(seat.getSeanceId())
                .place(seat.getPlace())
                .seatRow(seat.getSeatRow())
                .seatStatus(SeatStatus.valueOf(seat.getSeatStatus()))
                .build();
    }

    static Ticket toTicket(GetTicket getTicket) {
        return Ticket
                .builder()
                .id(getTicket.getId())
                .price(getTicket.getPrice())
                .ticketType(getTicket.getTicketType().toString())
                .build();
    }

    static GetTicket toGetTicket(Ticket ticket) {
        return GetTicket
                .builder()
                .id(ticket.getId())
                .price(ticket.getPrice())
                .ticketType(TicketType.valueOf(ticket.getTicketType()))
                .build();
    }

    static User toUser(GetUser getUser) {
        return User
                .builder()
                .id(getUser.getId())
                .username(getUser.getUsername())
                .age(getUser.getAge())
                .name(getUser.getName())
                .surname(getUser.getSurname())
                .email(getUser.getEmail())
                .role(getUser.getRole().toString())
                .build();
    }

    static GetUser toGetUser(User user) {
        return GetUser
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .age(user.getAge())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .role(Role.valueOf(user.getRole()))
                .build();
    }

    static MovieRating toMovieRating(GetMovieRating getMovieRating) {
        return MovieRating
                .builder()
                .id(getMovieRating.getId())
                .rate(getMovieRating.getRate())
                .movieId(getMovieRating.getMovieId())
                .userId(getMovieRating.getUserId())
                .build();
    }

    static GetMovieRating toGetMovieRating(MovieRating movieRating) {
        return GetMovieRating
                .builder()
                .id(movieRating.getId())
                .rate(movieRating.getRate())
                .movieId(movieRating.getMovieId())
                .userId(movieRating.getUserId())
                .build();
    }

    static ToWatch toWatch(GetToWatch getToWatch) {
        return ToWatch
                .builder()
                .id(getToWatch.getId())
                .userId(getToWatch.getUserId())
                .movieId(getToWatch.getMovieId())
                .build();
    }

    static GetToWatch toGetToWatch(ToWatch toWatch) {
        return GetToWatch
                .builder()
                .id(toWatch.getId())
                .movieId(toWatch.getMovieId())
                .userId(toWatch.getUserId())
                .build();
    }

    static FavoriteMovies toFavoriteMovies(GetFavoriteMovies getFavoriteMovies) {
        return FavoriteMovies
                .builder()
                .id(getFavoriteMovies.getId())
                .movieId(getFavoriteMovies.getMovieId())
                .userId(getFavoriteMovies.getUserId())
                .build();
    }

    static GetFavoriteMovies toGetFavoriteMovies(FavoriteMovies favoriteMovies) {
        return GetFavoriteMovies
                .builder()
                .id(favoriteMovies.getId())
                .movieId(favoriteMovies.getMovieId())
                .userId(favoriteMovies.getUserId())
                .build();
    }

    static Reservation toReservation(GetReservation getReservation) {
        return Reservation
                .builder()
                .id(getReservation.getId())
                .cityId(getReservation.getCityId())
                .cinemaId(getReservation.getCinemaId())
                .cinemaRoomId(getReservation.getCinemaRoomId())
                .movieId(getReservation.getMovieId())
                .seanceId(getReservation.getSeanceId())
                .seatsId(getReservation.getSeatsId())
                .ticketId(getReservation.getTicketId())
                .userId(getReservation.getUserId())
                .build();
    }

    static GetReservation toGetReservation(Reservation reservation) {
        return GetReservation
                .builder()
                .id(reservation.getId())
                .cityId(reservation.getCityId())
                .cinemaId(reservation.getCinemaId())
                .cinemaRoomId(reservation.getCinemaRoomId())
                .movieId(reservation.getMovieId())
                .seanceId(reservation.getSeanceId())
                .seatsId(reservation.getSeatsId())
                .ticketId(reservation.getTicketId())
                .userId(reservation.getUserId())
                .build();
    }
}
