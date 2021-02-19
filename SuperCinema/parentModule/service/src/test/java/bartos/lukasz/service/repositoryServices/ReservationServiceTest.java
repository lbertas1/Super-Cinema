package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateReservation;
import bartos.lukasz.dto.getModel.GetReservation;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Reservation;
import bartos.lukasz.model.Seat;
import bartos.lukasz.model.Ticket;
import bartos.lukasz.repository.ReservationRepository;
import bartos.lukasz.repository.SeatRepository;
import bartos.lukasz.repository.TicketRepository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ReservationService reservationService;

    private CreateReservation createReservation1;
    private CreateReservation createReservation2;
    private CreateReservation createReservation3;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;
    private Reservation reservation4;
    private Reservation reservation5;
    private Reservation reservation6;

    private Seat seat1;
    private Seat seat2;
    private Seat seat3;
    private Seat seat4;
    private Seat seat5;
    private Seat seat6;
    private Seat seat7;
    private Seat seat8;
    private Seat seat9;

    private Ticket ticket;

    private GetReservation getReservation1;
    private GetReservation getReservation2;
    private GetReservation getReservation3;

    private List<CreateReservation> createReservations;
    private List<Reservation> reservations;
    private List<Reservation> reservations1;
    private List<GetReservation> getReservations;

    private List<Seat> seats1;
    private List<Seat> seats2;
    private List<Seat> seats3;
    private List<Seat> allSeats;

    @BeforeEach
    private void prepareData() {
        createReservation1 = CreateReservation
                .builder()
                .cityId(1L)
                .cinemaId(1L)
                .cinemaRoomId(1L)
                .movieId(1L)
                .seanceId(1L)
                .seatsId(List.of(1L, 2L, 3L))
                .ticketId(1L)
                .userId(1L)
                .build();

        createReservation2 = CreateReservation
                .builder()
                .cityId(2L)
                .cinemaId(2L)
                .cinemaRoomId(2L)
                .movieId(2L)
                .seanceId(2L)
                .seatsId(List.of(4L, 5L, 6L))
                .ticketId(1L)
                .userId(2L)
                .build();

        createReservation3 = CreateReservation
                .builder()
                .cityId(2L)
                .cinemaId(2L)
                .cinemaRoomId(2L)
                .movieId(2L)
                .seanceId(2L)
                .seatsId(List.of(7L, 8L, 9L))
                .ticketId(1L)
                .userId(1L)
                .build();

        reservation1 = CreateModelMappers.toReservation(createReservation1);
        reservation2 = CreateModelMappers.toReservation(createReservation2);
        reservation3 = CreateModelMappers.toReservation(createReservation1);
        reservation3.setId(1L);
        reservation4 = CreateModelMappers.toReservation(createReservation2);
        reservation4.setId(2L);
        reservation5 = CreateModelMappers.toReservation(createReservation3);
        reservation6 = CreateModelMappers.toReservation(createReservation3);
        reservation6.setId(3L);

        getReservation1 = GetModelMappers.toGetReservation(reservation3);
        getReservation2 = GetModelMappers.toGetReservation(reservation4);
        getReservation3 = GetModelMappers.toGetReservation(reservation6);

        createReservations = List.of(createReservation1, createReservation2, createReservation3);
        reservations1 = List.of(reservation1, reservation2, reservation5);
        reservations = List.of(reservation3, reservation4, reservation6);
        getReservations = List.of(getReservation1, getReservation2, getReservation3);

        seat1 = Seat.builder().id(1L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat2 = Seat.builder().id(2L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat3 = Seat.builder().id(3L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat4 = Seat.builder().id(4L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat5 = Seat.builder().id(5L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat6 = Seat.builder().id(6L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat7 = Seat.builder().id(7L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat8 = Seat.builder().id(8L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();
        seat9 = Seat.builder().id(9L).place(1).roomId(1L).seanceId(1L).seatNumber(1).seatRow(1).seatStatus(SeatStatus.BUSY.name()).build();

        seats1 = List.of(seat1, seat2, seat3);
        seats2 = List.of(seat4, seat5, seat6);
        seats3 = List.of(seat7, seat8, seat9);

        allSeats = new ArrayList<>();
        allSeats.addAll(seats1);
        allSeats.addAll(seats2);
        allSeats.addAll(seats3);

        ticket = Ticket.builder().id(1L).price(new BigDecimal("20")).ticketType(TicketType.O.name()).build();
    }

    @Test
    public void save() {
        when(reservationRepository.save(reservation1)).thenReturn(Optional.of(reservation3));
        getReservation1
                .getSeatsId()
                .forEach(aLong -> {
                    Optional<Seat> selectedSeat = seats1
                            .stream()
                            .filter(seat -> seat.getId().equals(aLong))
                            .findFirst();

                    when(seatRepository.findById(aLong)).thenReturn(selectedSeat);
                });

        assertAll(() -> {
            assertEquals(getReservation1, reservationService.save(createReservation1));
            assertNotNull(reservationService.save(createReservation1).getId());
            assertNotNull(reservationService.save(createReservation1).getSeatsId());
        });
    }

    @Test
    public void updateTicket() {
        reservation3.setMovieId(10L);
        getReservation1.setMovieId(10L);
        when(reservationRepository.update(reservation3.getId(), reservation3)).thenReturn(Optional.of(reservation3));
        getReservation1
                .getSeatsId()
                .forEach(aLong -> {
                    Optional<Seat> selectedSeat = seats1
                            .stream()
                            .filter(seat -> seat.getId().equals(aLong))
                            .findFirst();

                    when(seatRepository.findById(aLong)).thenReturn(selectedSeat);
                });

        assertEquals(getReservation1, reservationService.update(getReservation1));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    public void findById(long id) {
        GetReservation expected = getReservations
                .stream()
                .filter(getReservation -> getReservation.getId().equals(id))
                .findFirst()
                .orElseThrow();

        Reservation reservation = reservations
                .stream()
                .filter(getReservation -> getReservation.getId().equals(id))
                .findFirst()
                .orElseThrow();

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        assertEquals(expected, reservationService.findById(id));
    }

    @ParameterizedTest()
    @ValueSource(longs = {1, 2})
    public void getAllUserReservation(long userId) {
        List<Reservation> userReservations = reservations
                .stream()
                .filter(reservation -> reservation.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<GetReservation> expected = userReservations
                .stream()
                .map(GetModelMappers::toGetReservation)
                .collect(Collectors.toList());

        when(reservationRepository.findByUserId(userId)).thenReturn(userReservations);
        assertEquals(expected, reservationService.getAllUserReservation(userId));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    public void remove(long id) {
        Optional<Reservation> selectedReservation = reservations
                .stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();

        when(reservationRepository.findById(id)).thenReturn(selectedReservation);

        selectedReservation
                .orElseThrow()
                .getSeatsId()
                .forEach(aLong -> {
                    Optional<Seat> selectedSeats = allSeats
                            .stream()
                            .filter(seat -> seat.getId().equals(aLong))
                            .findFirst();

                    when(seatRepository.findById(aLong)).thenReturn(selectedSeats);

                    selectedSeats.orElseThrow().setSeatStatus(SeatStatus.EMPTY.name());

                    Seat seatAfterUpdate = selectedSeats.orElseThrow();

                    when(seatRepository.update(aLong, seatAfterUpdate)).thenReturn(Optional.of(seatAfterUpdate));
                });

        when(ticketRepository.delete(selectedReservation.orElseThrow().getTicketId())).thenReturn(Optional.of(ticket));
        when(reservationRepository.delete(id)).thenReturn(selectedReservation);

        GetReservation result = getReservations
                .stream()
                .filter(getReservation -> getReservation.getId().equals(id))
                .findFirst()
                .orElseThrow();

        assertEquals(result, reservationService.remove(id));
    }


}
