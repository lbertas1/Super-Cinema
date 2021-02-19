package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateToWatch;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetToWatch;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Movie;
import bartos.lukasz.model.ToWatch;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.ToWatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ToWatchServiceTest {

    @Mock
    private ToWatchRepository toWatchRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ToWatchService toWatchService;

    private CreateToWatch createToWatch1;
    private CreateToWatch createToWatch2;
    private CreateToWatch createToWatch3;
    private CreateToWatch createToWatch4;
    private CreateToWatch createToWatch5;

    private ToWatch toWatch1;
    private ToWatch toWatch2;
    private ToWatch toWatch3;
    private ToWatch toWatch4;
    private ToWatch toWatch5;
    private ToWatch toWatch6;
    private ToWatch toWatch7;

    private GetToWatch getToWatch1;
    private GetToWatch getToWatch2;
    private GetToWatch getToWatch3;
    private GetToWatch getToWatch4;
    private GetToWatch getToWatch5;

    private List<CreateToWatch> createToWatches;
    private List<ToWatch> toWatches;
    private List<GetToWatch> getToWatches;

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;
    private Movie movie4;
    private Movie movie5;

    private GetMovie getMovie1;
    private GetMovie getMovie2;
    private GetMovie getMovie3;
    private GetMovie getMovie4;
    private GetMovie getMovie5;

    private List<Movie> movies;
    private List<GetMovie> getMovies;

    @BeforeEach
    private void prepareData() {
        movie1 = Movie.builder().id(1L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        movie2 = Movie.builder().id(2L).title("Inception").filmGenre(FilmGenre.THRILLER.toString()).build();
        movie3 = Movie.builder().id(3L).title("Thriller1").filmGenre(FilmGenre.THRILLER.toString()).build();
        movie4 = Movie.builder().id(4L).title("Anabelle").filmGenre(FilmGenre.HORROR.toString()).build();
        movie5 = Movie.builder().id(5L).title("Insidious").filmGenre(FilmGenre.HORROR.toString()).build();

        getMovie1 = GetModelMappers.toGetMovie(movie1);
        getMovie2 = GetModelMappers.toGetMovie(movie2);
        getMovie3 = GetModelMappers.toGetMovie(movie3);
        getMovie4 = GetModelMappers.toGetMovie(movie4);
        getMovie5 = GetModelMappers.toGetMovie(movie5);

        movies = List.of(movie1, movie2, movie3, movie4, movie5);
        getMovies = List.of(getMovie1, getMovie2, getMovie3, getMovie4, getMovie5);

        createToWatch1 = CreateToWatch.builder().userId(1L).movieId(1L).build();
        createToWatch2 = CreateToWatch.builder().userId(2L).movieId(2L).build();
        createToWatch3 = CreateToWatch.builder().userId(1L).movieId(3L).build();
        createToWatch4 = CreateToWatch.builder().userId(1L).movieId(4L).build();
        createToWatch5 = CreateToWatch.builder().userId(2L).movieId(5L).build();

        toWatch1 = CreateModelMappers.toWatch(createToWatch1);
        toWatch1.setId(1L);
        toWatch2 = CreateModelMappers.toWatch(createToWatch2);
        toWatch2.setId(2L);
        toWatch3 = CreateModelMappers.toWatch(createToWatch3);
        toWatch3.setId(3L);
        toWatch4 = CreateModelMappers.toWatch(createToWatch4);
        toWatch4.setId(4L);
        toWatch5 = CreateModelMappers.toWatch(createToWatch5);
        toWatch5.setId(5L);
        toWatch6 = CreateModelMappers.toWatch(createToWatch1);
        toWatch7 = CreateModelMappers.toWatch(createToWatch2);

        getToWatch1 = GetModelMappers.toGetToWatch(toWatch1);
        getToWatch2 = GetModelMappers.toGetToWatch(toWatch2);
        getToWatch3 = GetModelMappers.toGetToWatch(toWatch3);
        getToWatch4 = GetModelMappers.toGetToWatch(toWatch4);
        getToWatch5 = GetModelMappers.toGetToWatch(toWatch5);

        createToWatches = List.of(createToWatch1, createToWatch2, createToWatch3, createToWatch4, createToWatch5);
        toWatches = List.of(toWatch1, toWatch2, toWatch3, toWatch4, toWatch5);
        getToWatches = List.of(getToWatch1, getToWatch2, getToWatch3, getToWatch4, getToWatch5);
    }

    @Test
    public void save() {
        when(toWatchRepository.save(toWatch6)).thenReturn(Optional.of(toWatch1));
        assertEquals(getToWatch1, toWatchService.save(createToWatch1));
    }

    @RepeatedTest(10)
    public void remove() {
        int indexOfName = new Random().nextInt(getMovies.size());

        List<String> names = getMovies
                .stream()
                .map(GetMovie::getTitle)
                .collect(Collectors.toList());

        String name = names.get(indexOfName);

        when(movieRepository.findByName(name)).thenReturn(Optional.of(movies.get(indexOfName)));
        when(toWatchRepository.deleteToWatch(10L, movies.get(indexOfName).getId())).thenReturn(Optional.of(toWatches.get(indexOfName)));

        assertEquals(getToWatches.get(indexOfName), toWatchService.remove(name, 10L));
    }

    @Test
    public void getAllMovies() {
        Long userId = (long) new Random().nextInt(1) + 1;
        System.out.println(userId);

        List<ToWatch> toWatchMovies = toWatches
                .stream()
                .filter(favoriteMovies6 -> favoriteMovies6.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<Long> movieIds = toWatchMovies
                .stream()
                .map(ToWatch::getMovieId)
                .collect(Collectors.toList());

        List<Movie> userMovies = movies
                .stream()
                .filter(movie -> movieIds.contains(movie.getId()))
                .collect(Collectors.toList());

        List<GetMovie> result = userMovies
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());

        when(toWatchRepository.findAllByUserId(userId)).thenReturn(toWatchMovies);
        when(movieRepository.getAllByIdList(movieIds)).thenReturn(userMovies);

        assertEquals(result, toWatchService.getAllMovies(userId));
    }

    @ParameterizedTest
    @MethodSource("enumsValue")
    public void getAllByType(String filmGenre) {
        Long userId = (long) new Random().nextInt(2) + 1;

        List<ToWatch> userToWatchMovies = toWatches
                .stream()
                .filter(toWatch -> toWatch.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<Long> userMoviesId = userToWatchMovies
                .stream()
                .map(ToWatch::getMovieId)
                .collect(Collectors.toList());

        List<Movie> userMovies = movies
                .stream()
                .filter(movie -> movie.getFilmGenre().equals(filmGenre))
                .filter(movie -> userMoviesId.contains(movie.getId()))
                .collect(Collectors.toList());

        List<GetMovie> result = userMovies
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());

        when(toWatchRepository.findAllByUserId(userId)).thenReturn(userToWatchMovies);

        when(movieRepository.getMoviesByTypeAndMovieId(filmGenre, userMoviesId)).thenReturn(userMovies);

        assertEquals(result, toWatchService.getAllByType(FilmGenre.valueOf(filmGenre), userId));
    }

    static List<Arguments> enumsValue() {
        return List.of(
                Arguments.of(FilmGenre.THRILLER.toString()),
                Arguments.of(FilmGenre.HORROR.toString())
        );
    }
}