package holt.repository;

import holt.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findUserByUsernameAndPassword(String username, String password);
}