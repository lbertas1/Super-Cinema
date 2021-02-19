package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateSeance;
import bartos.lukasz.dto.createModel.CreateSeat;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.CinemaRoom;
import bartos.lukasz.model.Movie;
import bartos.lukasz.model.Seance;
import bartos.lukasz.repository.CinemaRoomRepository;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.SeanceRepository;
import bartos.lukasz.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SeanceServiceTest {

    @Mock
    private SeanceRepository seanceRepository;

    @Mock
    private CinemaRoomRepository cinemaRoomRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private SeanceService seanceService;

    private CreateSeance createSeance1;
    private CreateSeance createSeance2;

    private Seance seance1;
    private Seance seance2;
    private Seance seance3;
    private Seance seance4;

    private GetSeance getSeance1;
    private GetSeance getSeance2;

    private List<CreateSeance> createSeances;
    private List<Seance> seances;
    private List<Seance> seances1;
    private List<GetSeance> getSeances;

    @BeforeEach
    private void prepareData() {
        createSeance1 = CreateSeance
                .builder()
                .movieId(1L)
                .cinemaRoomId(1L)
                .screeningDate(LocalDate.now().plusDays(1).toString())
                .build();
        createSeance2 = CreateSeance
                .builder()
                .movieId(2L)
                .cinemaRoomId(2L)
                .screeningDate(LocalDate.now().plusDays(1).toString())
                .build();

        seance1 = CreateModelMappers.toSeance(createSeance1);
        seance2 = CreateModelMappers.toSeance(createSeance2);
        seance3 = CreateModelMappers.toSeance(createSeance1);
        seance3.setId(1L);
        seance4 = CreateModelMappers.toSeance(createSeance2);
        seance4.setId(2L);

        getSeance1 = GetModelMappers.toGetSeance(seance3);
        getSeance1.setMovieName("Braveheart");
        getSeance2 = GetModelMappers.toGetSeance(seance4);
        getSeance2.setMovieName("Braveheart");

        createSeances = List.of(createSeance1, createSeance2);
        seances = List.of(seance1, seance2);
        seances1 = List.of(seance3, seance4);
        getSeances = List.of(getSeance1, getSeance2);
    }

    @Test
    public void save() {
        CinemaRoom cinemaRoom = CinemaRoom.builder().id(1L).cinemaId(1L).roomNumber(1).placesInRow(8).quantityOfRows(8).build();
        Movie movie = Movie.builder().id(1L).filmGenre(FilmGenre.THRILLER.toString()).title("Braveheart").build();

        when(cinemaRoomRepository.findById(createSeance1.getCinemaRoomId())).thenReturn(Optional.of(cinemaRoom));
        when(movieRepository.findById(createSeance1.getMovieId())).thenReturn(Optional.of(movie));
        when(seanceRepository.save(seance1)).thenReturn(Optional.of(seance3));
        when(seatRepository.saveAll(null)).thenReturn(null);

        when(seanceRepository.save(seance1)).thenReturn(Optional.of(seance3));
        assertEquals(getSeance1, seanceService.save(createSeance1));
    }

    // działa działa
    @Test
    public void saveAll() {
        Movie movie = Movie.builder().id(1L).filmGenre(FilmGenre.THRILLER.toString()).title("Braveheart").build();
        Movie movie1 = Movie.builder().id(2L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        when(seanceRepository.saveAll(seances)).thenReturn(seances1);
        when(movieRepository.findById(seance3.getMovieId())).thenReturn(Optional.of(movie));
        when(movieRepository.findById(seance2.getMovieId())).thenReturn(Optional.of(movie1));

        assertEquals(getSeances, seanceService.saveAll(createSeances));
    }

    @Test
    public void findSeancesByGivenDate() {
        Movie movie = Movie.builder().id(1L).filmGenre(FilmGenre.THRILLER.toString()).title("Braveheart").build();
        Movie movie1 = Movie.builder().id(2L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        when(seanceRepository.findSeancesByDate(LocalDate.now().toString())).thenReturn(seances1);
        when(movieRepository.findById(seance3.getMovieId())).thenReturn(Optional.of(movie));
        when(movieRepository.findById(seance2.getMovieId())).thenReturn(Optional.of(movie1));

        assertEquals(getSeances, seanceService.findSeancesByGivenDate(LocalDate.now().toString()));
    }

    @Test
    public void findSeancesByGivenDate_shouldReturnEmptyList() {
        Movie movie = Movie.builder().id(1L).filmGenre(FilmGenre.THRILLER.toString()).title("Braveheart").build();
        Movie movie1 = Movie.builder().id(2L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        when(seanceRepository.findSeancesByDate(LocalDate.now().toString())).thenReturn(seances);
        when(movieRepository.findById(seance3.getMovieId())).thenReturn(Optional.of(movie));
        when(movieRepository.findById(seance2.getMovieId())).thenReturn(Optional.of(movie1));

        assertEquals(Collections.EMPTY_LIST, seanceService.findSeancesByGivenDate(LocalDate.now().plusDays(8).toString()));
    }

    @Test
    public void createSeatsForSeance() {
        GetCinemaRoom getCinemaRoom = GetCinemaRoom.builder().id(1L).cinemaId(1L).roomNumber(1).quantityOfRows(8).placesInRow(8).build();

        List<CreateSeat> seatsForSeance = seanceService.createSeatsForSeance(getCinemaRoom, 1L);

        int quantityOfPlacesForSeance = getCinemaRoom.getPlacesInRow() * getCinemaRoom.getQuantityOfRows();

        assertEquals(quantityOfPlacesForSeance, seatsForSeance.size());
    }

    @Test
    public void showSeances_throwUserDataException() {
        assertThrows(ServiceException.class, () -> seanceService.showSeances(null));
    }

    @Test
    public void showSeances_throwUserDataExceptionWhenListIsEmpty() {
        assertThrows(ServiceException.class, () -> seanceService.showSeances(Collections.emptyList()));
    }

    @Test
    public void chooseSeances_throwUserDataException() {
        assertThrows(ServiceException.class, () -> seanceService.showSeances(null));
    }

    @Test
    public void chooseSeances_throwUserDataExceptionWhenListIsEmpty() {
        assertThrows(ServiceException.class, () -> seanceService.showSeances(Collections.emptyList()));
    }

    // działa działa
    @Test
    public void getSeancesByFilmName() {
        GetMovie movieDto = GetMovie.builder().id(1L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();
        GetMovie movieDto1 = GetMovie.builder().id(2L).title("Braveheart").filmGenre(FilmGenre.THRILLER.toString()).build();

        List<Seance> seancesByIds = seances1
                .stream()
                .filter(seance -> seance.getMovieId().equals(movieDto.getId()))
                .collect(Collectors.toList());

        when(movieRepository.findByName(movieDto.getTitle())).thenReturn(Optional.of(GetModelMappers.toMovie(movieDto)));
        when(movieRepository.findByName(movieDto1.getTitle())).thenReturn(Optional.of(GetModelMappers.toMovie(movieDto1)));
        when(seanceRepository.findSeancesByMovieId(movieDto1.getId())).thenReturn(seancesByIds);
        when(movieRepository.findById(seance3.getMovieId())).thenReturn(Optional.of(GetModelMappers.toMovie(movieDto)));

        List<Long> moviesId = seanceService
                .getSeancesByFilmName(movieDto
                        .getTitle())
                .stream()
                .map(GetSeance::getMovieId)
                .collect(Collectors.toList());

        List<GetSeance> localGetSeances = List.of(getSeance1);

        assertAll(() -> {
            assertEquals(localGetSeances, seanceService.getSeancesByFilmName(movieDto.getTitle()));
            assertThat(moviesId, everyItem(equalTo(1L)));
            assertDoesNotThrow(() -> new ServiceException("Movie not found"));
            assertDoesNotThrow(() -> new ServiceException("Movie name is null"));
        });
    }
}