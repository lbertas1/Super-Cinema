package bartos.lukasz.repository.impl;

import bartos.lukasz.model.ToWatch;
import bartos.lukasz.repository.ToWatchRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ToWatchRepositoryImpl extends AbstractCrudRepository<ToWatch, Long> implements ToWatchRepository {
    public ToWatchRepositoryImpl(Jdbi connection) {
        super(connection);
    }

    @Override
    public Optional<ToWatch> deleteToWatch(Long userId, Long movieId) {
        var sqlGet = "select * from " + tableName + " where movie_id = :movieId;";
        Optional<ToWatch> toWatch = jdbi
                .withHandle(handle -> handle
                        .createQuery(sqlGet)
                        .bind("movieId", movieId)
                        .mapToBean(ToWatch.class)
                        .findFirst());

        var sqlRemove = "delete from " + tableName + " where movie_id = " + movieId + " and user_id = " + userId + ";";
        jdbi.withHandle(handle -> handle.execute(sqlRemove));

        return toWatch;
    }

    @Override
    public List<ToWatch> findAllByUserId(Long userId) {
        var sql = "select * from " + tableName + " where user_id = :userId;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(entityType)
                        .list());
    }
}
