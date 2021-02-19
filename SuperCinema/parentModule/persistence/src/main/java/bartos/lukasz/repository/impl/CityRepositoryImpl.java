package bartos.lukasz.repository.impl;

import bartos.lukasz.exception.RepositoryException;
import bartos.lukasz.model.City;
import bartos.lukasz.repository.CityRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepositoryImpl extends AbstractCrudRepository<City, Long> implements CityRepository {
    public CityRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public City getByName(String name) {
        var sql = "select * from " + tableName + " where name = :name";

        return jdbi
                .withHandle(handle -> handle
                .createQuery(sql)
                .bind("name", name)
                .mapToBean(entityType)
                .findOne()
                .orElseThrow(() -> new RepositoryException("Sorry, we do not have this city's repertoire in our database")));
    }
}
