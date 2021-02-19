package bartos.lukasz.repository;

import bartos.lukasz.model.ToWatch;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ToWatchRepository extends CrudRepository<ToWatch, Long> {

    Optional<ToWatch> deleteToWatch(Long userId, Long movieId);

    List<ToWatch> findAllByUserId(Long userId);
}
