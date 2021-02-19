package bartos.lukasz.repository.impl;

import bartos.lukasz.model.FavoriteMovies;
import bartos.lukasz.repository.FavoriteMoviesRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FavoriteMoviesRepositoryImpl extends AbstractCrudRepository<FavoriteMovies, Long> implements FavoriteMoviesRepository {
    public FavoriteMoviesRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public List<FavoriteMovies> findAllByUserId(Long userId) {
        var sql = "select * from " + tableName + " where user_id = :userId;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public Optional<FavoriteMovies> remove(Long userId, Long movieId) {
        var sqlGet = "select * from " + tableName + " where movie_id = :movieId;";
        Optional<FavoriteMovies> favoriteMovies = jdbi
                .withHandle(handle -> handle
                        .createQuery(sqlGet)
                        .bind("movieId", movieId)
                        .mapToBean(FavoriteMovies.class)
                        .findFirst());

        var sqlRemove = "delete from " + tableName + " where movie_id = " + movieId + " and user_id = " + userId + ";";
        jdbi.withHandle(handle -> handle.execute(sqlRemove));

        return favoriteMovies;
    }
}
