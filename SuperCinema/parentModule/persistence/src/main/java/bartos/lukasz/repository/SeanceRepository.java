package bartos.lukasz.repository;

import bartos.lukasz.model.Seance;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;

public interface SeanceRepository extends CrudRepository<Seance, Long> {

    List<Seance> findSeancesByDate(String date);

    List<Seance> findSeancesByMovieId(Long movieId);

    List<Seance> getSeancesByCinemaRoomId(List<Long> ids);

    List<Seance> findSeancesBeforeDate(String date);
}
