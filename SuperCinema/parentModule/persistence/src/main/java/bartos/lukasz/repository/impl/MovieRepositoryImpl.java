package bartos.lukasz.repository.impl;

import bartos.lukasz.model.Movie;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MovieRepositoryImpl extends AbstractCrudRepository<Movie, Long> implements MovieRepository {
    public MovieRepositoryImpl(Jdbi connection) {
        super(connection);
    }

    @Override
    public Optional<Movie> findByName(String movieName) {
        var sql = "select * from " + tableName + " where title = :movieName;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movieName", movieName)
                        .mapToBean(entityType)
                        .findFirst());
    }

    @Override
    public List<Movie> getMoviesByType(String type) {
        var sql = "select * from " + tableName + " where film_genre = :type;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("type", type)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public List<Movie> getAllByIdList(List<Long> ids) {
        var sql = "select * from " + tableName + " where id" + createClauseIn(ids);
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public List<Movie> getMoviesByTypeAndMovieId(String type, List<Long> ids) {
        var sql = "select * from " + tableName + " where film_genre = :type and id " + createClauseIn(ids);
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("type", type)
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
