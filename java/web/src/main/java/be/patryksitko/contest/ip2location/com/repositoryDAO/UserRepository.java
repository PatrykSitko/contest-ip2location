package be.patryksitko.contest.ip2location.com.repositoryDAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.patryksitko.contest.ip2location.com.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User AS u INNER JOIN u.credential AS c")
    User findByEmail(String email);
}
