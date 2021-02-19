package bartos.lukasz.repository;

import bartos.lukasz.model.Cinema;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.List;

public interface CinemaRepository extends CrudRepository<Cinema, Long> {

    Cinema getCinemaByName(String cinema);

    List<Cinema> getCinemaByCityName(String cityName);

    List<Cinema> getCinemaByCity(Long cityId);
}
