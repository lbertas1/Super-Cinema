package bartos.lukasz.repository.impl;

import bartos.lukasz.model.Reservation;
import bartos.lukasz.repository.ReservationRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationRepositoryImpl extends AbstractCrudRepository<Reservation, Long> implements ReservationRepository {
    public ReservationRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public List<Reservation> findByUserId(Long id) {
        var sql = "select * from " + tableName + " where user_id = :id;";

        return this
                .jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("id", id)
                        .registerRowMapper(Reservation.class, (rs, ctx) -> Reservation
                                .builder()
                                .id(rs.getLong("id"))
                                .cityId(rs.getLong("city_id"))
                                .cinemaId(rs.getLong("cinema_id"))
                                .cinemaRoomId(rs.getLong("cinema_room_id"))
                                .movieId(rs.getLong("movie_id"))
                                .seanceId(rs.getLong("seance_id"))
                                .seatsId(Arrays
                                        .stream(rs
                                                .getString("seats_id")
                                                .split(", "))
                                        .map(s -> s.replace("[", ""))
                                        .map(s -> s.replace("]", ""))
                                        .map(Long::valueOf).collect(Collectors.toList()))
                                .ticketId(rs.getLong("ticket_id"))
                                .userId(rs.getLong("user_id"))
                                .build())
                        .mapTo(entityType)
                        .list());
    }
}
