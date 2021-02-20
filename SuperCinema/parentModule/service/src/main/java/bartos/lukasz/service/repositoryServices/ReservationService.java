package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateReservation;
import bartos.lukasz.dto.getModel.GetReservation;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Reservation;
import bartos.lukasz.model.Seat;
import bartos.lukasz.model.User;
import bartos.lukasz.repository.ReservationRepository;
import bartos.lukasz.repository.SeatRepository;
import bartos.lukasz.repository.TicketRepository;
import bartos.lukasz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    public GetReservation save(CreateReservation createReservation) {
        changeSeatsStatus(createReservation.getSeatsId());

        Reservation reservation = CreateModelMappers.toReservation(createReservation);
        Reservation reservation1 = reservationRepository.save(reservation).orElseThrow();
        return GetModelMappers.toGetReservation(reservation1);
    }

    public GetReservation update(GetReservation getReservation) {
        changeSeatsStatus(getReservation.getSeatsId());
        return GetModelMappers.toGetReservation(reservationRepository.update(getReservation.getId(), GetModelMappers.toReservation(getReservation)).orElseThrow(() -> new ServiceException("Reservation doesn't updated")));
    }

    private void changeSeatsStatus(List<Long> seats) {
        seats
                .forEach(aLong -> {
                    Seat seat = seatRepository
                            .findById(aLong)
                            .orElseThrow(() -> new ServiceException("Seat doesn't found"));
                    if (!seat.getSeatStatus().equals(SeatStatus.BUSY.name())) {
                        seat.setSeatStatus(SeatStatus.BUSY.name());
                        seatRepository
                                .update(aLong, seat);
                    }
                });
    }

    public GetReservation findById(Long id) {
        return GetModelMappers.toGetReservation(reservationRepository.findById(id).orElseThrow(() -> new ServiceException("Reservation doesn't found")));
    }

    public List<GetReservation> getAllUserReservationById(Long id) {
        return reservationRepository
                .findByUserId(id)
                .stream()
                .map(GetModelMappers::toGetReservation)
                .collect(Collectors.toList());
    }

    public List<GetReservation> getAllUserReservationByUsername(String login) {
        User user = userRepository.findUserByUsername(login).orElseThrow(() -> new ServiceException("User doesn't found"));

        return reservationRepository
                .findByUserId(user.getId())
                .stream()
                .map(GetModelMappers::toGetReservation)
                .collect(Collectors.toList());
    }

    public GetReservation remove(Long id) {
        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ServiceException("Reservation doesn't found"));

        reservation
                .getSeatsId()
                .forEach(aLong -> {
                    Seat seat = seatRepository
                            .findById(aLong)
                            .orElseThrow(() -> new ServiceException("Seat doesn't found"));

                    seat.setSeatStatus(SeatStatus.EMPTY.toString());

                    Seat updatedSeats = seatRepository.update(aLong, seat).orElseThrow(() -> new ServiceException("Seats doesn't updated"));
                });

        Optional<Reservation> delete = reservationRepository.delete(id);

        ticketRepository
                .delete(reservation
                        .getTicketId())
                .orElseThrow(() -> new ServiceException("Ticket doesn't removed"));

        Reservation reservation1 = delete.orElseThrow();

        return GetModelMappers.toGetReservation(reservation1);
    }
}
