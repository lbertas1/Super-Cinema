package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateMovieRating;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetMovieRating;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.repository.MovieRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieRatingService {

    private final MovieRatingRepository movieRatingRepository;

    public GetMovieRating save(CreateMovieRating createMovieRating) {
        return GetModelMappers
                .toGetMovieRating(movieRatingRepository
                        .save(CreateModelMappers
                                .toMovieRating(createMovieRating)).orElseThrow(() -> new ServiceException("Rate doesn't saved")));
    }

    public List<GetMovieRating> getAll() {
        return movieRatingRepository
                .findAll()
                .stream()
                .map(GetModelMappers::toGetMovieRating)
                .collect(Collectors.toList());
    }

    public List<GetMovieRating> getAllUserRatings(Long userId) {
        return movieRatingRepository
                .getAllUserRatings(userId)
                .stream()
                .map(GetModelMappers::toGetMovieRating)
                .collect(Collectors.toList());
    }

    public List<GetMovie> getMovieWithHighestRateByUser(Long userId) {
        return movieRatingRepository
                .getHighestRatingMovieByUser(userId)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public GetMovie getMovieWithHighestAvgRate() {
        return GetModelMappers
                .toGetMovie(movieRatingRepository
                        .getMovieWithHighestRate()
                        .orElseThrow(() -> new ServiceException("Movie doesn't found")));
    }

    public GetMovie getMovieWithHighestAvgRateInCategory(FilmGenre filmGenre) {
        return GetModelMappers
                .toGetMovie(movieRatingRepository
                        .getMovieWithHighestRateInFilmGenre(filmGenre)
                        .orElseThrow(() -> new ServiceException("Movie doesn't found")));
    }

    public GetMovie getMostOftenRatingMovie() {
        return GetModelMappers
                .toGetMovie(movieRatingRepository
                .getMostOftenRatingMovie()
                .orElseThrow(() -> new ServiceException("Movie doesn't found")));
    }
}
