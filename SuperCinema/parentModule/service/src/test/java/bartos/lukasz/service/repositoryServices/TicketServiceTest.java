package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateReservation;
import bartos.lukasz.dto.createModel.CreateTicket;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetReservation;
import bartos.lukasz.dto.getModel.GetTicket;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.enums.Role;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.*;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.ReservationRepository;
import bartos.lukasz.repository.TicketRepository;
import bartos.lukasz.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private TicketService ticketService;

    private CreateTicket createTicket1;
    private CreateTicket createTicket2;

    private CreateReservation createReservation1;
    private CreateReservation createReservation2;

    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Ticket ticket4;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;
    private Reservation reservation4;

    private Seat seat1;

    private GetTicket getTicket1;
    private GetTicket getTicket2;

    private GetReservation getReservation1;
    private GetReservation getReservation2;

    private List<CreateTicket> createTickets;
    private List<Ticket> tickets;
    private List<Ticket> tickets1;
    private List<GetTicket> getTickets;

    private List<GetReservation> reservations;

    @BeforeEach
    private void prepareData() {
        createTicket1 = CreateTicket.builder().price(new BigDecimal("100")).ticketType(TicketType.E).build();
        createTicket2 = CreateTicket.builder().price(new BigDecimal("200")).ticketType(TicketType.E).build();

        ticket1 = CreateModelMappers.toTicket(createTicket1);
        ticket2 = CreateModelMappers.toTicket(createTicket2);
        ticket3 = CreateModelMappers.toTicket(createTicket1);
        ticket3.setId(1L);
        ticket4 = CreateModelMappers.toTicket(createTicket2);
        ticket4.setId(2L);

        seat1 = Seat.builder().id(1L).place(8).roomId(1L).seanceId(1L).seatNumber(2).seatRow(2).seatStatus(SeatStatus.BUSY.name()).build();

        getTicket1 = GetModelMappers.toGetTicket(ticket3);
        getTicket2 = GetModelMappers.toGetTicket(ticket4);

        createTickets = List.of(createTicket1, createTicket2);
        tickets = List.of(ticket1, ticket2);
        tickets1 = List.of(ticket3, ticket4);
        getTickets = List.of(getTicket1, getTicket2);

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
                .ticketId(2L)
                .userId(2L)
                .build();

        reservation1 = CreateModelMappers.toReservation(createReservation1);
        reservation2 = CreateModelMappers.toReservation(createReservation2);
        reservation3 = CreateModelMappers.toReservation(createReservation1);
        reservation3.setId(1L);
        reservation4 = CreateModelMappers.toReservation(createReservation2);
        reservation4.setId(2L);

        getReservation1 = GetModelMappers.toGetReservation(reservation3);
        getReservation2 = GetModelMappers.toGetReservation(reservation4);

        reservations = List.of(getReservation1, getReservation2);
    }

    @Test
    public void save() {
        when(ticketRepository.save(ticket1)).thenReturn(Optional.of(ticket3));
        assertEquals(getTicket1, ticketService.save(createTicket1));
    }

    @Test
    public void saveAll() {
        when(ticketRepository.saveAll(tickets)).thenReturn(tickets1);
        assertEquals(getTickets, ticketService.saveAll(createTickets));
    }

    @Test
    public void getTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket3));
        assertEquals(getTicket1, ticketService.getTicket(1L));
    }

    @Test
    public void updateTicket() {
        BigDecimal newPrice = new BigDecimal("10000");
        ticket3.setPrice(newPrice);
        GetTicket getTicket3 = GetModelMappers.toGetTicket(ticket3);
        when(ticketRepository.update(1L, ticket3)).thenReturn(Optional.of(ticket3));
        assertAll(() -> {
            assertEquals(getTicket3, ticketService.updateTicket(1L, getTicket3));
            assertEquals(newPrice, ticketService.updateTicket(1L, getTicket3).getPrice());
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    public void getMoviesWatchedByUser(long userId) {
        // GIVEN
        String username = "username";

        User user = User
                .builder()
                .id(userId)
                .username("username")
                .name("lukas")
                .surname("kowalski")
                .password("lala")
                .age(23)
                .email("lala")
                .role(Role.USER.name())
                .build();

        List<Reservation> userReservations = reservations
                .stream()
                .filter(getReservation -> getReservation.getUserId().equals(userId))
                .map(GetModelMappers::toReservation)
                .collect(Collectors.toList());

        List<Long> moviesId = userReservations
                .stream()
                .map(Reservation::getMovieId)
                .collect(Collectors.toList());

        List<Movie> movies = List.of(
          Movie.builder().id(1L).title("title1").filmGenre(FilmGenre.ACTION.name()).build(),
          Movie.builder().id(2L).title("title2").filmGenre(FilmGenre.ACTION.name()).build(),
          Movie.builder().id(3L).title("title3").filmGenre(FilmGenre.ACTION.name()).build(),
          Movie.builder().id(4L).title("title4").filmGenre(FilmGenre.ACTION.name()).build(),
          Movie.builder().id(5L).title("title5").filmGenre(FilmGenre.ACTION.name()).build(),
          Movie.builder().id(6L).title("title6").filmGenre(FilmGenre.ACTION.name()).build()
        );

        List<Movie> moviesWatchedByUser = movies
                .stream()
                .filter(movie -> moviesId.contains(movie.getId()))
                .collect(Collectors.toList());

        List<GetMovie> getMovies = moviesWatchedByUser
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());

        // WHEN
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(reservationRepository.findByUserId(userId)).thenReturn(userReservations);
        when(movieRepository.findAllById(moviesId)).thenReturn(moviesWatchedByUser);

        // THEN
        assertEquals(getMovies, ticketService.getMoviesWatchedByUser(username));
    }
}