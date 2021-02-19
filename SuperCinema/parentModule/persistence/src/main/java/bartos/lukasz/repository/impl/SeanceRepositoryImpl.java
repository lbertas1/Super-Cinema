package bartos.lukasz.repository.impl;

import bartos.lukasz.model.Seance;
import bartos.lukasz.repository.SeanceRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SeanceRepositoryImpl extends AbstractCrudRepository<Seance, Long> implements SeanceRepository {
    public SeanceRepositoryImpl(Jdbi connection) {
        super(connection);
    }

    @Override
    public List<Seance> findSeancesByDate(String date) {
        var SQL = "select * from " + tableName + " where screening_date = :date;";
        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .bind("date", date)
                .mapToBean(entityType)
                .list());
    }

    @Override
    public List<Seance> findSeancesByMovieId(Long movieId) {
        var SQL = "select * from " + tableName + " where movie_id = :movieId;";
        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .bind("movieId", movieId)
                .mapToBean(entityType)
                .list());
    }

    @Override
    public List<Seance> getSeancesByCinemaRoomId(List<Long> ids) {
        var sql = "select * from " + tableName + " where cinema_room_id " + createClauseIn(ids);
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public List<Seance> findSeancesBeforeDate(String date) {
        var SQL = "select * from " + tableName + " where screening_date < :date;";
        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .bind("date", date)
                .mapToBean(entityType)
                .list());
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
