package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateFavoriteMovies;
import bartos.lukasz.dto.getModel.GetFavoriteMovies;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.FavoriteMovies;
import bartos.lukasz.repository.FavoriteMoviesRepository;
import bartos.lukasz.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteMoviesService {

    private final FavoriteMoviesRepository favoriteMoviesRepository;
    private final MovieRepository movieRepository;

    public GetFavoriteMovies save(CreateFavoriteMovies createFavoriteMovies) {
        return GetModelMappers.toGetFavoriteMovies(favoriteMoviesRepository.save(CreateModelMappers.toFavoriteMovies(createFavoriteMovies)).orElseThrow(() -> new ServiceException("Saving doesn't work")));
    }

    public GetFavoriteMovies remove(Long userId, String movieName) {
        Long movieId = GetModelMappers.toGetMovie(movieRepository.findByName(movieName).orElseThrow(() -> new ServiceException("Movie doesn't found"))).getId();
        return GetModelMappers.toGetFavoriteMovies(favoriteMoviesRepository.remove(userId, movieId).orElseThrow(() -> new ServiceException("Movie doesn't remove")));
    }

    public List<GetMovie> getAllMovies(Long userId) {
        List<Long> moviesId = favoriteMoviesRepository
                .findAllByUserId(userId)
                .stream()
                .map(FavoriteMovies::getMovieId)
                .collect(Collectors.toList());

        return movieRepository
                .getAllByIdList(moviesId)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public List<GetMovie> getAllByType(FilmGenre filmGenre, Long userId) {
        List<Long> moviesId = favoriteMoviesRepository
                .findAllByUserId(userId)
                .stream()
                .map(FavoriteMovies::getMovieId)
                .collect(Collectors.toList());

        return movieRepository
                .getMoviesByTypeAndMovieId(filmGenre.toString(), moviesId)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }
}
