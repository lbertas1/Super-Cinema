package bartos.lukasz.repository;

import bartos.lukasz.model.User;
import bartos.lukasz.repository.generic.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByNameAndSurname(String name, String surname);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUsernameAndPassword(String username, String password);
}
