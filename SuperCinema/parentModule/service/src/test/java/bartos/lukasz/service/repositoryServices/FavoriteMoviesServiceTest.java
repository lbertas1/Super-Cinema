package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateFavoriteMovies;
import bartos.lukasz.dto.getModel.GetFavoriteMovies;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.FavoriteMovies;
import bartos.lukasz.model.Movie;
import bartos.lukasz.repository.FavoriteMoviesRepository;
import bartos.lukasz.repository.MovieRepository;
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
class FavoriteMoviesServiceTest {

    @Mock
    private FavoriteMoviesRepository favoriteMoviesRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private FavoriteMoviesService favoriteMoviesService;

    private CreateFavoriteMovies createFavoriteMovies1;
    private CreateFavoriteMovies createFavoriteMovies2;
    private CreateFavoriteMovies createFavoriteMovies3;
    private CreateFavoriteMovies createFavoriteMovies4;
    private CreateFavoriteMovies createFavoriteMovies5;

    private FavoriteMovies favoriteMovies1;
    private FavoriteMovies favoriteMovies2;
    private FavoriteMovies favoriteMovies3;
    private FavoriteMovies favoriteMovies4;
    private FavoriteMovies favoriteMovies5;
    private FavoriteMovies favoriteMovies6;
    private FavoriteMovies favoriteMovies7;

    private GetFavoriteMovies getFavoriteMovies1;
    private GetFavoriteMovies getFavoriteMovies2;
    private GetFavoriteMovies getFavoriteMovies3;
    private GetFavoriteMovies getFavoriteMovies4;
    private GetFavoriteMovies getFavoriteMovies5;

    private List<CreateFavoriteMovies> createFavoriteMovies;
    private List<FavoriteMovies> favoriteMovies;
    private List<GetFavoriteMovies> getFavoriteMovies;

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

        createFavoriteMovies1 = CreateFavoriteMovies.builder().userId(1L).movieId(1L).build();
        createFavoriteMovies2 = CreateFavoriteMovies.builder().userId(2L).movieId(2L).build();
        createFavoriteMovies3 = CreateFavoriteMovies.builder().userId(1L).movieId(3L).build();
        createFavoriteMovies4 = CreateFavoriteMovies.builder().userId(1L).movieId(4L).build();
        createFavoriteMovies5 = CreateFavoriteMovies.builder().userId(2L).movieId(5L).build();

        favoriteMovies1 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies1);
        favoriteMovies1.setId(1L);
        favoriteMovies2 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies2);
        favoriteMovies2.setId(2L);
        favoriteMovies3 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies3);
        favoriteMovies3.setId(3L);
        favoriteMovies4 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies4);
        favoriteMovies4.setId(4L);
        favoriteMovies5 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies5);
        favoriteMovies5.setId(5L);
        favoriteMovies6 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies1);
        favoriteMovies7 = CreateModelMappers.toFavoriteMovies(createFavoriteMovies2);

        getFavoriteMovies1 = GetModelMappers.toGetFavoriteMovies(favoriteMovies1);
        getFavoriteMovies2 = GetModelMappers.toGetFavoriteMovies(favoriteMovies2);
        getFavoriteMovies3 = GetModelMappers.toGetFavoriteMovies(favoriteMovies3);
        getFavoriteMovies4 = GetModelMappers.toGetFavoriteMovies(favoriteMovies4);
        getFavoriteMovies5 = GetModelMappers.toGetFavoriteMovies(favoriteMovies5);

        createFavoriteMovies = List.of(createFavoriteMovies1, createFavoriteMovies2, createFavoriteMovies3, createFavoriteMovies4, createFavoriteMovies5);
        favoriteMovies = List.of(favoriteMovies1, favoriteMovies2, favoriteMovies3, favoriteMovies4, favoriteMovies5);
        getFavoriteMovies = List.of(getFavoriteMovies1, getFavoriteMovies2, getFavoriteMovies3, getFavoriteMovies4, getFavoriteMovies5);
    }

    @Test
    public void save() {
        when(favoriteMoviesRepository.save(favoriteMovies6)).thenReturn(Optional.of(favoriteMovies1));
        assertEquals(getFavoriteMovies1, favoriteMoviesService.save(createFavoriteMovies1));
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
        when(favoriteMoviesRepository.remove(10L, movies.get(indexOfName).getId())).thenReturn(Optional.of(favoriteMovies.get(indexOfName)));

        assertEquals(getFavoriteMovies.get(indexOfName), favoriteMoviesService.remove(10L, name));
    }

    @Test
    public void getAllMovies() {
        Long userId = (long) new Random().nextInt(1) + 1;
        System.out.println(userId);

        List<FavoriteMovies> faMovies = favoriteMovies
                .stream()
                .filter(favoriteMovies6 -> favoriteMovies6.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<Long> movieIds = faMovies
                .stream()
                .map(FavoriteMovies::getMovieId)
                .collect(Collectors.toList());

        List<Movie> userMovies = movies
                .stream()
                .filter(movie -> movieIds.contains(movie.getId()))
                .collect(Collectors.toList());

        List<GetMovie> result = userMovies
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());

        when(favoriteMoviesRepository.findAllByUserId(userId)).thenReturn(faMovies);
        when(movieRepository.getAllByIdList(movieIds)).thenReturn(userMovies);

        assertEquals(result, favoriteMoviesService.getAllMovies(userId));
    }

    @ParameterizedTest
    @MethodSource("enumsValue")
    public void getAllByType(String filmGenre) {
        Long userId = (long) new Random().nextInt(2) + 1;

        List<FavoriteMovies> userFavoriteMovies = favoriteMovies
                .stream()
                .filter(favoriteMovies6 -> favoriteMovies6.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<Long> userMoviesId = userFavoriteMovies
                .stream()
                .map(FavoriteMovies::getMovieId)
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

        when(favoriteMoviesRepository.findAllByUserId(userId)).thenReturn(userFavoriteMovies);

        when(movieRepository.getMoviesByTypeAndMovieId(filmGenre, userMoviesId)).thenReturn(userMovies);

        assertEquals(result, favoriteMoviesService.getAllByType(FilmGenre.valueOf(filmGenre), userId));
    }

    static List<Arguments> enumsValue() {
        return List.of(
          Arguments.of(FilmGenre.THRILLER.toString()),
          Arguments.of(FilmGenre.HORROR.toString())
        );
    }
}