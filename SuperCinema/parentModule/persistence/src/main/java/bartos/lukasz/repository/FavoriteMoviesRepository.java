package bartos.lukasz.repository;

import bartos.lukasz.model.FavoriteMovies;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteMoviesRepository extends CrudRepository<FavoriteMovies, Long> {

    List<FavoriteMovies> findAllByUserId(Long userId);

    Optional<FavoriteMovies> remove(Long userId, Long movieId);
}
