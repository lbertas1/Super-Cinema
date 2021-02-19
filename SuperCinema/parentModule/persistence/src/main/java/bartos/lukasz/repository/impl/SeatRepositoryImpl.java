package bartos.lukasz.repository.impl;

import bartos.lukasz.model.Seat;
import bartos.lukasz.repository.SeatRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SeatRepositoryImpl extends AbstractCrudRepository<Seat, Long> implements SeatRepository {
    public SeatRepositoryImpl(Jdbi connection) {
        super(connection);
    }

    @Override
    public List<Seat> getAllBusySeatsForSeance(Long seanceId) {
        var sql = "select * from seats where seance_id = :seanceId and seat_status = 'BUSY';";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seanceId", seanceId)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public List<Seat> getSeatsBySeanceId(Long seanceId) {
        var sql = "select * from " + tableName + " where seance_id = :seanceId;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seanceId", seanceId)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public List<Seat> getAllSeatsByStatus(Long seanceId, String seatStatus) {
        var sql = "select * from " + tableName + " where seance_id = :seanceId and seat_status = :seatStatus;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seanceId", seanceId)
                        .bind("seatStatus", seatStatus)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public List<Seat> removeAllBySeanceId(List<Long> ids) {
        var sql = "select * from " + tableName + " where seance_id" + createClauseIn(ids);
        List<Seat> removedSeats = jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .mapToBean(entityType)
                        .list());

        var removeSql = "delete from " + tableName + " where seance_id" + createClauseIn(ids);
        jdbi.withHandle(handle -> handle.execute(removeSql));

        return removedSeats;
    }

    @Override
    public Optional<Seat> getSeatByNumber(Long seanceId, Integer number) {
        var sql = "select * from " + tableName + " where seance_id = :seanceId and seat_number = :number;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seanceId", seanceId)
                        .bind("number", number)
                        .mapToBean(entityType)
                        .findFirst());
    }

    private String createClauseIn(List<Long> ids) {
        String clause = " IN (";
        clause = clause.concat(ids
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
        clause = clause.concat(")");
        return clause;
    }
}
