package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateMovie;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Movie;
import bartos.lukasz.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private CreateMovie createMovie1;
    private CreateMovie createMovie2;

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;
    private Movie movie4;

    private GetMovie getMovie1;
    private GetMovie getMovie2;

    private List<CreateMovie> createMovies;
    private List<Movie> movies;
    private List<Movie> movies2;
    private List<GetMovie> getMovies;

    @BeforeEach
    private void prepareData() {
        createMovie1 = CreateMovie.builder().title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        createMovie2 = CreateMovie.builder().title("Inception").filmGenre(FilmGenre.THRILLER.toString()).build();

        movie1 = CreateModelMappers.toMovie(createMovie1);
        movie2 = CreateModelMappers.toMovie(createMovie2);
        movie3 = CreateModelMappers.toMovie(createMovie1);
        movie3.setId(1L);
        movie4 = CreateModelMappers.toMovie(createMovie2);
        movie4.setId(2L);

        getMovie1 = GetModelMappers.toGetMovie(movie3);
        getMovie2 = GetModelMappers.toGetMovie(movie4);

        createMovies = List.of(createMovie1, createMovie2);
        movies = List.of(movie1, movie2);
        movies2 = List.of(movie3, movie4);
        getMovies = List.of(getMovie1, getMovie2);
    }

    @Test
    public void save() {
        when(movieRepository.save(movie1)).thenReturn(Optional.of(movie3));
        assertEquals(getMovie1, movieService.save(createMovie1));
    }

    @Test
    public void saveAll() {
        when(movieRepository.saveAll(movies)).thenReturn(movies2);
        assertEquals(getMovies, movieService.saveAll(createMovies));
    }

    @Test
    public void findById() {
        when(movieRepository.findById(movie1.getId())).thenReturn(Optional.of(movie3));

        assertEquals(getMovie1, movieService.findById(movie1.getId()));
    }

    @Test
    public void getAllMovies() {
        when(movieRepository.findAll()).thenReturn(movies2);

        assertEquals(getMovies, movieService.getAllMovies());
    }

    @Test
    public void findMovieByName() {
        when(movieRepository.findByName(getMovie1.getTitle())).thenReturn(Optional.of(movie3));

        assertEquals(getMovie1, movieService.findMovieByName(getMovie1.getTitle()));
    }

    @Test
    public void findMovieByName_throwsExceptionWhenIsNull() {
        when(movieRepository.findByName(null)).thenThrow(new ServiceException("Movie not found"));

        assertThrows(ServiceException.class, () -> movieService.findMovieByName(null));
    }

    @Test
    public void getMoviesByType() {
        when(movieRepository.getMoviesByType(FilmGenre.THRILLER.toString())).thenReturn(movies2);

        assertEquals(getMovies, movieService.getMoviesByType(FilmGenre.THRILLER.name()));
    }

    @Test
    public void getMoviesByType_throwExceptionWhenTypeIsIncorrect() {
        assertThrows(ServiceException.class, () -> movieService.getMoviesByType("musical"));
    }

    @Test
    public void getAllByIdList() {
        GetSeance seanceDto = GetSeance.builder().movieId(1L).build();
        GetSeance seanceDto1 = GetSeance.builder().movieId(2L).build();
        List<GetSeance> seanceDtos = List.of(seanceDto, seanceDto1);

        when(movieRepository
                .getAllByIdList(seanceDtos
                        .stream()
                        .map(GetSeance::getMovieId)
                        .collect(Collectors.toList())))
                .thenReturn(movies2);

        assertEquals(getMovies, movieService.getAllByMoviesIdList(seanceDtos));
    }

    @Test
    public void showMovies_whereMoviesAreNull() {
        assertThrows(ServiceException.class, () -> movieService.showMovies(null));
    }

    @Test
    public void showMovies_whereMoviesListIsEmpty() {
        assertThrows(ServiceException.class, () -> movieService.showMovies(Collections.emptyList()));
    }
}