package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateTicket;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetTicket;
import bartos.lukasz.dto.getModel.GetUser;
import bartos.lukasz.enums.TicketType;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Reservation;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.ReservationRepository;
import bartos.lukasz.repository.TicketRepository;
import bartos.lukasz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReservationRepository reservationRepository;

    public GetTicket save(CreateTicket createTicket) {
         return GetModelMappers
                .toGetTicket(ticketRepository
                        .save(CreateModelMappers
                                .toTicket(createTicket))
                        .orElseThrow(() -> new ServiceException("Ticket cannot be saved")));
    }

    public List<GetTicket> saveAll(List<CreateTicket> ticketDtos) {
        return ticketRepository
                .saveAll(ticketDtos
                        .stream()
                        .map(CreateModelMappers::toTicket)
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetTicket)
                .collect(Collectors.toList());
    }

    public GetTicket getTicket(Long ticketId) {
        return GetModelMappers.toGetTicket(ticketRepository.findById(ticketId).orElseThrow(() -> new ServiceException("Ticket doesn't found")));
    }

    public GetTicket updateTicket(Long ticketId, GetTicket getTicket) {
        return GetModelMappers.toGetTicket(ticketRepository.update(ticketId, GetModelMappers.toTicket(getTicket)).orElseThrow(() -> new ServiceException("Ticket doesn't updated")));
    }

    public List<GetMovie> getMoviesWatchedByUser(String username) {
        GetUser getUser = GetModelMappers.toGetUser(userRepository.findUserByUsername(username).orElseThrow(() -> new ServiceException("User not found")));

        List<Long> moviesId = reservationRepository
                .findByUserId(getUser.getId())
                .stream()
                .map(Reservation::getMovieId)
                .collect(Collectors.toList());

        if (moviesId.isEmpty()) return new ArrayList<>();

        return movieRepository
                .findAllById(moviesId)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public GetTicket createTicket() {
        BigDecimal price = new BigDecimal(String.valueOf(new Random().nextInt(8) + 15));

        CreateTicket createTicket = CreateTicket
                .builder()
                .price(price)
                .ticketType(TicketType.R)
                .build();

        return GetModelMappers
                .toGetTicket(ticketRepository
                        .save(CreateModelMappers
                                .toTicket(createTicket))
                        .orElseThrow(() -> new ServiceException("Ticket doesn't saved")));
    }

    public BigDecimal countTicketPrice(BigDecimal ticketPrice, int seatNumber) {
        return ticketPrice.multiply(new BigDecimal(seatNumber));
    }
}
