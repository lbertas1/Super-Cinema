package bartos.lukasz.repository;

import bartos.lukasz.model.Movie;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    Optional<Movie> findByName(String movieName);

    List<Movie> getMoviesByType(String type);

    List<Movie> getAllByIdList(List<Long> ids);

    List<Movie> getMoviesByTypeAndMovieId(String type, List<Long> ids);

//    List<Movie> getMoviesFromCity(Long cityId);
}
