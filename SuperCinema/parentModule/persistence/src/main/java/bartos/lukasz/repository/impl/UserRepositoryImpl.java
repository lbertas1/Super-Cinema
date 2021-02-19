package bartos.lukasz.repository.impl;

import bartos.lukasz.model.User;
import bartos.lukasz.repository.UserRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractCrudRepository<User, Long> implements UserRepository {
    public UserRepositoryImpl(Jdbi connection) {
        super(connection);
    }

    @Override
    public Optional<User> findUserByNameAndSurname(String name, String surname) {
        var sql = "select * from " + tableName + " where name = :name and surname = :surname;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("name", name)
                        .bind("surname", surname)
                        .mapToBean(entityType)
                        .findFirst());
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        var sql = "select * from " + tableName + " where username = :username;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("username", username)
                        .mapToBean(entityType)
                        .findFirst());
    }

    @Override
    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        var sql = "select * from " + tableName + " where username = :username and password = :password;";
        return jdbi
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("username", username)
                        .bind("password", password)
                        .mapToBean(entityType)
                        .findFirst());
    }
}
