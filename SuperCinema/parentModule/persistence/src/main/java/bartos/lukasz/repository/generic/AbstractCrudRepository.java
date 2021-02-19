package bartos.lukasz.repository.generic;

import bartos.lukasz.exception.RepositoryException;
import bartos.lukasz.model.Reservation;
import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {

    protected final Jdbi jdbi;

    @SuppressWarnings("unchecked")
    protected Class<T> entityType = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @SuppressWarnings("unchecked")
    protected Class<ID> idType = (Class<ID>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    protected String tableName = English.plural(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityType.getSimpleName()));

    @Override
    public Optional<T> save(T item) {
        var SQL = "insert into " +
                tableName + " " +
                generateColumnNamesForInsert() +
                " values " +
                replaceSemicolonWithComma(generateValuesForInsert(item)) + ";";

        var insertedRows = jdbi.withHandle(handle -> handle.execute(SQL));

        if (insertedRows > 0) {
            return findLast();
        }
        return Optional.empty();
    }

    @Override
    public List<T> saveAll(List<T> items) {
        StringBuilder values = new StringBuilder(items
                .stream()
                .map(t -> generateValuesForInsert(t) + ",\n"
                )
                .collect(Collectors.joining())
        );

        values.deleteCharAt(values.lastIndexOf(","));

        var SQL = "insert into " +
                tableName + " " +
                generateColumnNamesForInsert() +
                " values " +
                values.toString() + ";";

        jdbi.withHandle(handle -> handle.execute(SQL));
        return items;
    }

    @Override
    public Optional<T> update(ID id, T item) {
        var SQL = "update " + tableName + " set " + prepareUpdateRow(generateColumnNamesForInsert(), generateValuesForInsert(item)) + " where id = :id;";

        jdbi
                .withHandle(handle -> handle
                .createUpdate(SQL)
                .bind("id", id)
                .execute());

        return findById(id);
    }

    @Override
    public Optional<T> findById(ID id) {
        var SQL = "select * from " + tableName + " where id = :id;";

        if (entityType.isInstance(Reservation.builder().build())) {
            return findReservation(SQL, id);
        }

        return jdbi
                .withHandle(handle -> handle
                .createQuery(SQL)
                .bind("id", id)
                .mapToBean(entityType)
                .findFirst());
    }

    @Override
    public List<T> findAll() {
        var SQL = "select * from " + tableName;
        return jdbi
                .withHandle(handle -> handle
                .createQuery(SQL)
                .mapToBean(entityType)
                .list());
    }

    @Override
    public List<T> findAllById(List<ID> ids) {
        StringBuilder idString = new StringBuilder(ids
                .stream()
                .map(id -> "'" + id + "', ")
                .collect(Collectors.joining()));
        idString.deleteCharAt(idString.lastIndexOf(","));

        var SQL = "select * from " + tableName + " where id IN ( " + idString.toString() + " );";

        return jdbi
                .withHandle(handle -> handle.createQuery(SQL)
                        .mapToBean(entityType)
                        .list());
    }

    @Override
    public Optional<T> delete(ID id) {
        Optional<T> byId = findById(id);
        var SQL = "delete from " + tableName + " where id = " + id;
        jdbi.withHandle(handle -> handle.execute(SQL));
        return byId;
    }

    @Override
    public List<T> deleteAll(List<ID> ids) {
        List<T> allById = findAllById(ids);

        StringBuilder idString = new StringBuilder(ids
                .stream()
                .map(id -> "'" + id + "', ")
                .collect(Collectors.joining()));
        idString.deleteCharAt(idString.lastIndexOf(","));

        var SQL = "delete from " + tableName + " where id IN ( " + idString.toString() + " );";

        jdbi.withHandle(handle -> handle.execute(SQL));

        return allById;
    }

    private Optional<T> findLast() {
        if (entityType.isInstance(Reservation.builder().build())) {
            return findLastReservation("select * from " + tableName + " order by id desc limit 1");
        }

        return jdbi.withHandle(handle -> handle
                .createQuery("select * from " + tableName + " order by id desc limit 1")
                .mapToBean(entityType)
                .findFirst());
    }

    private Optional<T> findReservation(String query, ID id) {
        return this
                .jdbi
                .withHandle(handle -> handle
                        .createQuery(query)
                        .bind("id", id)
                        .registerRowMapper(Reservation.class, (rs, ctx) -> Reservation
                                .builder()
                                .id(rs.getLong("id"))
                                .cityId(rs.getLong("city_id"))
                                .cinemaId(rs.getLong("cinema_id"))
                                .cinemaRoomId(rs.getLong("cinema_room_id"))
                                .movieId(rs.getLong("movie_id"))
                                .seanceId(rs.getLong("seance_id"))
                                .seatsId(Arrays
                                        .stream(rs
                                                .getString("seats_id")
                                                .split(", "))
                                        .map(s -> s.replace("[", ""))
                                        .map(s -> s.replace("]", ""))
                                        .map(Long::valueOf).collect(Collectors.toList()))
                                .ticketId(rs.getLong("ticket_id"))
                                .userId(rs.getLong("user_id"))
                                .build())
                        .mapTo(entityType)
                        .findFirst());
    }

    private Optional<T> findLastReservation(String query) {
        return this
                .jdbi
                .withHandle(handle -> handle
                        .createQuery(query)
                        .registerRowMapper(Reservation.class, (rs, ctx) -> Reservation
                                .builder()
                                .id(rs.getLong("id"))
                                .cityId(rs.getLong("city_id"))
                                .cinemaId(rs.getLong("cinema_id"))
                                .cinemaRoomId(rs.getLong("cinema_room_id"))
                                .movieId(rs.getLong("movie_id"))
                                .seanceId(rs.getLong("seance_id"))
                                .seatsId(Arrays
                                        .stream(rs
                                                .getString("seats_id")
                                                .split(", "))
                                        .map(s -> s.replace("[", ""))
                                        .map(s -> s.replace("]", ""))
                                        .map(Long::valueOf).collect(Collectors.toList()))
                                .ticketId(rs.getLong("ticket_id"))
                                .userId(rs.getLong("user_id"))
                                .build())
                        .mapTo(entityType)
                        .findFirst());
    }

    private String generateColumnNamesForInsert() {
        return "( " + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(Field::getName)
                .map(s -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s))
                .collect(Collectors.joining(", ")) + ") ";
    }

    private String generateValuesForInsert(T item) {
        return "( " + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        if (field.getType().equals(String.class) || field.getType().equals(LocalDate.class)) {
                            return "'" + field.get(item) + "'";
                        }

                        if (field.getType().equals(List.class)) {

                            String listString = field.get(item).toString().replace(",", ";");

                            return "'" + listString + "'";
                        }


                        return String.valueOf(field.get(item));
                    } catch (Exception e) {
                        throw new RepositoryException(e.getMessage());
                    }
                })
                .collect(Collectors.joining(", ")) + ") ";
    }

    private String replaceSemicolonWithComma(String row) {
        return row.replaceAll(";", ",");
    }

    private String prepareUpdateRow(String columns, String values) {
        String finalSql = "";
        List<String> columnAfterEdit = prepareWords(columns);
        List<String> valuesAfterEdit = prepareWords(values);

        for (int i = 0; i < columnAfterEdit.size(); i++) {
            finalSql = finalSql.concat(columnAfterEdit.get(i));
            finalSql = finalSql.concat(" = ");
            finalSql = finalSql.concat(valuesAfterEdit.get(i));
            finalSql = finalSql.concat(", ");
        }
        return new StringBuilder(finalSql).reverse().replace(0, 2, "").reverse().toString();
    }

    private List<String> prepareWords(String word) {
        String[] words = word.split(",");

        if (words.length > 5 && words[5].contains(";")) {
            words[5] = words[5].replaceAll(";", ", ");
        }

        return Arrays
                .stream(words)
                .map(s -> {
                    s = s.replaceAll("\\s+", "");
                    s = s.replaceAll("\\(+", "");
                    return s = s.replaceAll("\\)+", "");
                })
                .collect(Collectors.toList());
    }
}
