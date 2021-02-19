package bartos.lukasz.repository.impl;

import bartos.lukasz.exception.RepositoryException;
import bartos.lukasz.model.Cinema;
import bartos.lukasz.repository.CinemaRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CinemaRepositoryImpl extends AbstractCrudRepository<Cinema, Long> implements CinemaRepository {

    public CinemaRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    public Cinema getCinemaByName(String cinema) {
        var sql = "select * from " + tableName + " where name = :cinema;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cinema", cinema)
                        .mapToBean(entityType)
                        .findOne()
                        .orElseThrow(() -> new RepositoryException("Incorrect name. Cinema not found")));
    }

    @Override
    public List<Cinema> getCinemaByCityName(String cityName) {
        var sql = "select cinemas.id, cinemas.name, cinemas.city_id from cinemas " +
                "left join cities on cinemas.city_id = cities.id " +
                "where cities.name = :cityName;";

        return jdbi
                .withHandle(handle -> handle
                .createQuery(sql)
                .bind("cityName", cityName)
                .mapToBean(Cinema.class)
                .list());
    }

    @Override
    public List<Cinema> getCinemaByCity(Long cityId) {
        var sql = "select * from " + tableName + " where id = :cityId;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cityId", cityId)
                        .mapToBean(entityType)
                        .list());
    }
}
