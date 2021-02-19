package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateSeat;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.dto.getModel.GetSeat;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Seance;
import bartos.lukasz.model.Seat;
import bartos.lukasz.repository.SeanceRepository;
import bartos.lukasz.repository.SeatRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeanceRepository seanceRepository;

    @InjectMocks
    private SeatService seatService;

    private CreateSeat createSeat1;
    private CreateSeat createSeat2;

    private Seat seat1;
    private Seat seat2;
    private Seat seat3;
    private Seat seat4;

    private GetSeat getSeat1;
    private GetSeat getSeat2;

    private List<CreateSeat> createSeats;
    private List<Seat> seats;
    private List<Seat> seats1;
    private List<GetSeat> getSeats;

    @BeforeEach
    private void prepareData() {
        createSeat1 = CreateSeat.builder().seatRow(2).seatNumber(2).place(2).seanceId(1L).roomId(1L).build();
        createSeat2 = CreateSeat.builder().seatRow(3).seatNumber(3).place(3).seanceId(2L).roomId(2L).build();

        seat1 = CreateModelMappers.toSeat(createSeat1);
        seat2 = CreateModelMappers.toSeat(createSeat2);
        seat3 = CreateModelMappers.toSeat(createSeat1);
        seat3.setId(1L);
        seat3.setSeatNumber(1);
        seat3.setSeatStatus(SeatStatus.EMPTY.toString());
        seat4 = CreateModelMappers.toSeat(createSeat2);
        seat4.setId(2L);
        seat4.setSeatNumber(2);
        seat4.setSeatStatus(SeatStatus.EMPTY.toString());

        getSeat1 = GetModelMappers.toGetSeat(seat3);
        getSeat2 = GetModelMappers.toGetSeat(seat4);

        createSeats = List.of(createSeat1, createSeat2);
        seats = List.of(seat1, seat2);
        seats1 = List.of(seat3, seat4);
        getSeats = List.of(getSeat1, getSeat2);
    }

    @Test
    public void save() {
        when(seatRepository.save(seat1)).thenReturn(Optional.of(seat3));
        assertThat(seatService.save(createSeat1), equalTo(getSeat1));
    }

    @Test
    public void saveAll() {
        when(seatRepository.saveAll(seats)).thenReturn(seats1);
        assertThat(seatService.saveAll(createSeats), equalTo(getSeats));
    }

    @Test
    public void getSeat() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat3));
        assertEquals(getSeat1, seatService.getSeat(1L));
    }

    @Test
    public void getSeats() {
        List<Long> ids = seats
                .stream()
                .map(Seat::getId)
                .collect(Collectors.toList());

        when(seatRepository.findAllById(ids)).thenReturn(seats1);
        assertEquals(getSeats, seatService.getSeats(ids));
    }

    @Test
    public void getBusySeatsFromSeance() {
        GetSeance seanceDto = GetSeance.builder().movieId(1L).screeningDate(LocalDate.now()).build();

        when(seatRepository.getAllBusySeatsForSeance(seanceDto.getId())).thenReturn(seats1);

        List<Long> seatsId = seatService
                .getSeatsBySeanceId(seanceDto
                        .getId())
                .stream()
                .map(GetSeat::getSeanceId)
                .collect(Collectors.toList());

        assertAll(() -> {
            assertEquals(getSeats, seatService.getBusySeatsFromSeance(seanceDto.getId()));
            assertThat(seatsId, everyItem(equalTo(1L)));
        });
    }

    @Test
    public void getSeatsBySeanceId() {
        GetSeance seanceDto = GetSeance.builder().movieId(1L).screeningDate(LocalDate.now()).build();

        when(seatRepository.getSeatsBySeanceId(seanceDto.getId())).thenReturn(seats1);

        assertThat(getSeats, equalTo(seatService.getSeatsBySeanceId(seanceDto.getId())));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    public void removeById(long seatId) {
        Seat chosenSeat = seats1
                .stream()
                .filter(seatDto3 -> seatDto3
                        .getId()
                        .equals(seatId))
                .findFirst()
                .orElseThrow();

        when(seatRepository.findAll()).thenReturn(seats1);
        when(seatRepository.delete(seatId)).thenReturn(Optional.of(chosenSeat));

        List<GetSeat> remainingSeats = getSeats
                .stream()
                .filter(seatDto3 -> !seatDto3.getId().equals(seatId))
                .collect(Collectors.toList());

        assertAll(() -> {
            assertEquals(GetModelMappers.toGetSeat(chosenSeat),
                    seatService.removeById(seatId));
            assertThat(remainingSeats, hasSize(getSeats.size() - 1));
        });
    }

    @Test
    public void getAllSeatsByStatus() {
        List<Seat> filteredSeats = seats1
                .stream()
                .filter(getSeat -> getSeat.getSeanceId().equals(1L) && getSeat.getSeatStatus().equals(SeatStatus.EMPTY.toString()))
                .collect(Collectors.toList());

        List<GetSeat> filteredGetSeats = getSeats
                .stream()
                .filter(getSeat -> getSeat.getSeanceId().equals(1L) && getSeat.getSeatStatus().equals(SeatStatus.EMPTY))
                .collect(Collectors.toList());

        when(seatRepository.getAllSeatsByStatus(1L, SeatStatus.EMPTY.toString())).thenReturn(filteredSeats);

        assertEquals(filteredGetSeats, seatService.getAllSeatsByStatus(1L, SeatStatus.EMPTY));
    }

    @Test
    public void clearDatabase() {
        Seance seance1 = Seance.builder().id(1L).cinemaRoomId(1L).movieId(1L).screeningDate(LocalDate.now().minusDays(1)).build();
        Seance seance2 = Seance.builder().id(2L).cinemaRoomId(2L).movieId(2L).screeningDate(LocalDate.now().minusDays(2)).build();
        Seance seance3 = Seance.builder().id(3L).cinemaRoomId(3L).movieId(3L).screeningDate(LocalDate.now().minusDays(3)).build();
        List<Seance> seances = List.of(seance1, seance2, seance3);

        List<Seance> seancesResult = seances
                .stream()
                .filter(seance -> !seance.getScreeningDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        List<Long> ids = seancesResult
                .stream()
                .map(Seance::getId)
                .collect(Collectors.toList());

        List<GetSeat> result = getSeats
                .stream()
                .filter(getSeat -> ids.contains(getSeat.getId()))
                .collect(Collectors.toList());

        when(seanceRepository.findSeancesBeforeDate(LocalDate.now().toString())).thenReturn(seancesResult);

        assertEquals(result, seatService.clearDatabase());
    }

    @Test
    public void getSeatByNumber() {
        when(seatRepository.getSeatByNumber(1L, 1)).thenReturn(Optional.of(seat3));
        assertEquals(getSeat1, seatService.getSeatByNumber(1L, 1));
    }

    @Test
    public void showSeats_seatStatusIsNull() {
        assertThrows(ServiceException.class, () -> seatService.showSeats(null, 1L));
    }

    @Test
    public void showSeats_seanceIdIsNull() {
        assertThrows(ServiceException.class, () -> seatService.showSeats(SeatStatus.EMPTY, null));
    }
}