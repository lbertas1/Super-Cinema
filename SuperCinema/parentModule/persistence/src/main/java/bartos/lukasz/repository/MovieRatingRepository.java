package bartos.lukasz.repository;

import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.model.Movie;
import bartos.lukasz.model.MovieRating;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRatingRepository extends CrudRepository<MovieRating, Long> {

    Optional<Movie> getMovieWithHighestRate();

    List<MovieRating> getAllUserRatings(Long userId);

    Optional<Movie> getMovieWithHighestRateInFilmGenre(FilmGenre filmGenre);

    List<Movie> getHighestRatingMovieByUser(Long userId);

    Optional<Movie> getMostOftenRatingMovie();


}
