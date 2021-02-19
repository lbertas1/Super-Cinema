package bartos.lukasz.repository;

import bartos.lukasz.model.City;
import bartos.lukasz.repository.generic.CrudRepository;

public interface CityRepository extends CrudRepository<City, Long> {

    City getByName (String name);

}
