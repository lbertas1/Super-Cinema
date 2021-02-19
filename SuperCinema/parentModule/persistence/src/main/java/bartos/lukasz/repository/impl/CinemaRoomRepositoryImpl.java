package bartos.lukasz.repository.impl;

import bartos.lukasz.model.CinemaRoom;
import bartos.lukasz.repository.CinemaRoomRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CinemaRoomRepositoryImpl extends AbstractCrudRepository<CinemaRoom, Long> implements CinemaRoomRepository {
    public CinemaRoomRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public List<CinemaRoom> getCinemaRoomsByCinemaId(Long cinemaId) {
        var SQL = "select * from " + tableName + " where cinema_id = :cinemaId;";
        return jdbi.withHandle(handle -> handle
                .createQuery(SQL)
                .bind("cinemaId", cinemaId)
                .mapToBean(entityType)
                .list());
    }

    @Override
    public List<CinemaRoom> getCinemaRoomsByCinemaName(String cinemaName) {
        var sql = "Select cr.id, cr.room_number, cr.quantity_of_rows, cr.places_in_row, cr.cinema_id" +
                " from cinema_rooms as cr" +
                " left join cinemas on cr.cinema_id = cinemas.id" +
                " where cinemas.name = :cinemaName;";

        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("cinemaName", cinemaName)
                .mapToBean(CinemaRoom.class)
                .list());
    }
}
