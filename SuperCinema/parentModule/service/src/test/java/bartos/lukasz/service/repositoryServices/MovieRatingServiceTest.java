package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateMovieRating;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetMovieRating;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Movie;
import bartos.lukasz.model.MovieRating;
import bartos.lukasz.repository.MovieRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MovieRatingServiceTest {

    @Mock
    private MovieRatingRepository movieRatingRepository;

    @InjectMocks
    private MovieRatingService movieRatingService;

    private CreateMovieRating createMovieRating1;
    private CreateMovieRating createMovieRating2;
    private CreateMovieRating createMovieRating3;
    private CreateMovieRating createMovieRating4;
    private CreateMovieRating createMovieRating5;
    private CreateMovieRating createMovieRating6;

    private MovieRating movieRating1;
    private MovieRating movieRating2;
    private MovieRating movieRating3;
    private MovieRating movieRating4;
    private MovieRating movieRating5;
    private MovieRating movieRating6;
    private MovieRating movieRating7;
    private MovieRating movieRating8;

    private GetMovieRating getMovieRating1;
    private GetMovieRating getMovieRating2;
    private GetMovieRating getMovieRating3;
    private GetMovieRating getMovieRating4;
    private GetMovieRating getMovieRating5;
    private GetMovieRating getMovieRating6;

    private GetMovie getMovie1;
    private GetMovie getMovie2;
    private GetMovie getMovie3;

    private List<MovieRating> movieRatings;
    private List<GetMovieRating> getMovieRatings;
    private List<GetMovie> getMovies;

    @BeforeEach
    private void prepareData() {
        createMovieRating1 = CreateMovieRating.builder().rate(8).movieId(1L).userId(1L).build();
        createMovieRating2 = CreateMovieRating.builder().rate(7).movieId(2L).userId(2L).build();
        createMovieRating3 = CreateMovieRating.builder().rate(6).movieId(3L).userId(1L).build();
        createMovieRating4 = CreateMovieRating.builder().rate(9).movieId(2L).userId(1L).build();
        createMovieRating5 = CreateMovieRating.builder().rate(9).movieId(3L).userId(1L).build();
        createMovieRating6 = CreateMovieRating.builder().rate(3).movieId(2L).userId(2L).build();

        movieRating1 = CreateModelMappers.toMovieRating(createMovieRating1);
        movieRating1.setId(1L);
        movieRating2 = CreateModelMappers.toMovieRating(createMovieRating2);
        movieRating2.setId(2L);
        movieRating3 = CreateModelMappers.toMovieRating(createMovieRating3);
        movieRating3.setId(3L);
        movieRating4 = CreateModelMappers.toMovieRating(createMovieRating4);
        movieRating4.setId(4L);
        movieRating5 = CreateModelMappers.toMovieRating(createMovieRating5);
        movieRating5.setId(5L);
        movieRating6 = CreateModelMappers.toMovieRating(createMovieRating6);
        movieRating6.setId(6L);
        movieRating7 = CreateModelMappers.toMovieRating(createMovieRating1);
        movieRating8 = CreateModelMappers.toMovieRating(createMovieRating2);

        getMovieRating1 = GetModelMappers.toGetMovieRating(movieRating1);
        getMovieRating2 = GetModelMappers.toGetMovieRating(movieRating2);
        getMovieRating3 = GetModelMappers.toGetMovieRating(movieRating3);
        getMovieRating4 = GetModelMappers.toGetMovieRating(movieRating4);
        getMovieRating5 = GetModelMappers.toGetMovieRating(movieRating5);
        getMovieRating6 = GetModelMappers.toGetMovieRating(movieRating6);

        getMovie1 = GetMovie.builder().id(1L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        getMovie2 = GetMovie.builder().id(2L).title("Inception").filmGenre(FilmGenre.THRILLER.toString()).build();
        getMovie3 = GetMovie.builder().id(3L).title("StarWars").filmGenre(FilmGenre.SCI_FI.toString()).build();

        movieRatings = List.of(movieRating1, movieRating2, movieRating3, movieRating4, movieRating5, movieRating6);
        getMovieRatings = List.of(getMovieRating1, getMovieRating2, getMovieRating3, getMovieRating4, getMovieRating5, getMovieRating6);
        getMovies = List.of(getMovie1, getMovie2, getMovie3);
    }

    @Test
    public void save() {
        when(movieRatingRepository.save(movieRating7)).thenReturn(Optional.of(movieRating1));
        assertEquals(getMovieRating1, movieRatingService.save(createMovieRating1));
    }

    @Test
    public void getAll() {
        when(movieRatingRepository.findAll()).thenReturn(movieRatings);
        assertEquals(getMovieRatings, movieRatingService.getAll());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    public void getAllUserRatings(long userId) {
        List<GetMovieRating> userRatings = getMovieRatings
                .stream()
                .filter(getMovieRating -> getMovieRating.getUserId().equals(userId))
                .collect(Collectors.toList());

        when(movieRatingRepository
                .getAllUserRatings(userId))
                .thenReturn(movieRatings
                        .stream()
                        .filter(getMovieRating -> getMovieRating.getUserId().equals(userId))
                        .collect(Collectors.toList()));

        assertEquals(userRatings, movieRatingService.getAllUserRatings(userId));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    public void getMovieWithHighestRateByUser(long userId) {
        int maxRate = getMovieRatings
                .stream()
                .filter(getMovieRating -> getMovieRating.getUserId().equals(userId))
                .mapToInt(GetMovieRating::getRate)
                .max()
                .orElseThrow();

        List<Long> movieIds = getMovieRatings
                .stream()
                .filter(getMovieRating -> getMovieRating.getUserId().equals(userId))
                .filter(getMovieRating -> getMovieRating.getRate().equals(maxRate))
                .map(GetMovieRating::getMovieId)
                .collect(Collectors.toList());

        List<Movie> resultList = getMovies
                .stream()
                .filter(getMovie -> movieIds.contains(getMovie.getId()))
                .map(GetModelMappers::toMovie)
                .collect(Collectors.toList());

        List<GetMovie> finalGetMovieList = resultList
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());

        when(movieRatingRepository.getHighestRatingMovieByUser(userId)).thenReturn(resultList);
        assertEquals(finalGetMovieList, movieRatingService.getMovieWithHighestRateByUser(userId));
    }

    @Test
    public void getMovieWithHighestAvgRate() {
        Long movieId = getMovieRatings
                .stream()
                .collect(Collectors.groupingBy(
                        GetMovieRating::getMovieId,
                        Collectors.collectingAndThen(Collectors.averagingDouble(GetMovieRating::getRate), Function.identity())
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();

        GetMovie resultGetMovie = getMovies
                .stream()
                .filter(getMovie -> getMovie.getId().equals(movieId))
                .findFirst()
                .orElseThrow();

        when(movieRatingRepository.getMovieWithHighestRate()).thenReturn(Optional.of(GetModelMappers.toMovie(resultGetMovie)));

        assertEquals(resultGetMovie, movieRatingService.getMovieWithHighestAvgRate());
    }

    @Test
    public void getMovieWithHighestAvgRateInCategory() {
        List<Long> idsMoviesFromCategory = getMovies
                .stream()
                .filter(getMovie -> getMovie.getFilmGenre().equals(FilmGenre.THRILLER.toString()))
                .map(GetMovie::getId)
                .collect(Collectors.toList());

        List<GetMovieRating> correctMovies = getMovieRatings
                .stream()
                .filter(getMovieRating -> idsMoviesFromCategory.contains(getMovieRating.getId()))
                .collect(Collectors.toList());

        Long movieId = correctMovies
                .stream()
                .collect(Collectors.groupingBy(
                        GetMovieRating::getMovieId,
                        Collectors.collectingAndThen(Collectors.averagingDouble(GetMovieRating::getRate), Function.identity())
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();

        GetMovie resultGetMovie = getMovies
                .stream()
                .filter(getMovie -> getMovie.getId().equals(movieId))
                .findFirst()
                .orElseThrow();

        when(movieRatingRepository.getMovieWithHighestRate()).thenReturn(Optional.of(GetModelMappers.toMovie(resultGetMovie)));

        assertEquals(resultGetMovie, movieRatingService.getMovieWithHighestAvgRate());
    }

    @Test
    public void getMostOftenRatingMovie() {
        Long movieId = getMovieRatings
                .stream()
                .collect(Collectors.groupingBy(
                        GetMovieRating::getMovieId,
                        Collectors.collectingAndThen(Collectors.counting(), Function.identity())
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();

        GetMovie result = getMovies
                .stream()
                .filter(getMovie -> getMovie.getId().equals(movieId))
                .findFirst()
                .orElseThrow();

        when(movieRatingRepository.getMostOftenRatingMovie()).thenReturn(Optional.of(GetModelMappers.toMovie(result)));

        assertEquals(result, movieRatingService.getMostOftenRatingMovie());
    }
}