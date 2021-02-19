package bartos.lukasz.repository;

import bartos.lukasz.model.Reservation;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long id);
}
