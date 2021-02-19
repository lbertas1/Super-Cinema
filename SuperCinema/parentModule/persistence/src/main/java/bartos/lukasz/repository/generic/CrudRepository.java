package bartos.lukasz.repository.generic;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> save(T item);
    List<T> saveAll(List<T> items);
    Optional<T> update(ID id, T item);
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findAllById(List<ID> ids);
    Optional<T> delete(ID id);
    List<T> deleteAll(List<ID> ids);
}
