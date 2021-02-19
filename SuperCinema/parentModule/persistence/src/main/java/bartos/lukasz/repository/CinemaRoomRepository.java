package bartos.lukasz.repository;

import bartos.lukasz.model.CinemaRoom;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;

public interface CinemaRoomRepository extends CrudRepository<CinemaRoom, Long> {

    List<CinemaRoom> getCinemaRoomsByCinemaId(Long cinemaId);

    List<CinemaRoom> getCinemaRoomsByCinemaName(String cinemaName);
}
