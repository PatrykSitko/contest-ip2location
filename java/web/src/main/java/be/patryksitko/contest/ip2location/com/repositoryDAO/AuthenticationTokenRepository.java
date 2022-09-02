package be.patryksitko.contest.ip2location.com.repositoryDAO;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.patryksitko.contest.ip2location.com.model.AuthenticationToken;

@Repository
public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationToken, Long> {

    @Query("SELECT authToken FROM AuthenticationToken AS authToken INNER JOIN authToken.credential AS credential")
    List<AuthenticationToken> findByEmail(String email);
}
