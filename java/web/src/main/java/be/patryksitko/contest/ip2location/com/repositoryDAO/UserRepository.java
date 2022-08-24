package be.patryksitko.contest.ip2location.com.repositoryDAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.patryksitko.contest.ip2location.com.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
