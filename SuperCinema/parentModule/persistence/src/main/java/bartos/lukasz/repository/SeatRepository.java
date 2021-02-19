package bartos.lukasz.repository;

import bartos.lukasz.model.Seat;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends CrudRepository<Seat, Long> {

    List<Seat> getAllBusySeatsForSeance(Long seanceId);

    List<Seat> getSeatsBySeanceId(Long seanceId);

    List<Seat> getAllSeatsByStatus(Long seanceId, String seatStatus);

    List<Seat> removeAllBySeanceId(List<Long> ids);

    Optional<Seat> getSeatByNumber(Long seanceId, Integer number);
}
