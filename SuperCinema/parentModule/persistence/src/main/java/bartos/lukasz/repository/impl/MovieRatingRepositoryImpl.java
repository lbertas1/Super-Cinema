package bartos.lukasz.repository.impl;

import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.model.Movie;
import bartos.lukasz.model.MovieRating;
import bartos.lukasz.repository.MovieRatingRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MovieRatingRepositoryImpl extends AbstractCrudRepository<MovieRating, Long> implements MovieRatingRepository {
    public MovieRatingRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Optional<Movie> getMovieWithHighestRate() {
        var sql = """
                select
                *
                from (select movie_id , avg(movie_ratings.rate) as `avgRate`
                from movie_ratings
                group by movie_ratings.movie_id) as `maxAvg`, movies
                where movies.id = movie_id
                group by movie_id
                order by avgRate desc
                limit 1;
                """;

        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .mapToBean(Movie.class)
                        .findFirst());
    }

    @Override
    public List<MovieRating> getAllUserRatings(Long userId) {
        var sql = "select * from " + tableName + " where user_id = :userId;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public Optional<Movie> getMovieWithHighestRateInFilmGenre(FilmGenre filmGenre) {
        String filmType = filmGenre.name();

        var sql = """
                select
                movies.id,
                movies.title,
                movies.film_genre
                from movie_ratings as mr
                left join movies on movies.id = mr.movie_id
                where movies.film_genre = :filmType
                order by mr.rate desc
                limit 1; 
                """;

        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("filmType", filmType)
                        .mapToBean(Movie.class)
                        .findFirst());
    }

    @Override
    public List<Movie> getHighestRatingMovieByUser(Long userId) {
        var sql = """
                select
                *
                from (select movie_id , avg(movie_ratings.rate) as `avgRate`
                from movie_ratings
                where movie_ratings.user_id = :userId
                group by movie_ratings.movie_id) as `maxAvg`, movies
                where movies.id = movie_id
                group by movie_id
                order by avgRate desc
                limit 1;
                             """;

        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Movie.class)
                        .list());
    }

    @Override
    public Optional<Movie> getMostOftenRatingMovie() {
        var sql = """
                select
                *
                from (select movie_id , count(movie_id) as `avgRate`
                from movie_ratings
                group by movie_ratings.movie_id) as `maxAvg`, movies
                where movies.id = movie_id
                group by movie_id
                order by avgRate desc
                limit 1;
                """;

        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .mapToBean(Movie.class)
                        .findFirst());
    }
}
