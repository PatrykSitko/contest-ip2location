package be.patryksitko.contest.ip2location.com.service;

import java.util.Optional;

import be.patryksitko.contest.ip2location.com.model.AuthenticationToken;
import be.patryksitko.contest.ip2location.com.model.Credential;
import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.EmailUnregisteredException;

public interface UserService {

    public User registerUser(User user) throws EmailRegisteredException;

    public User findUserByEmail(String email) throws EmailUnregisteredException;

    public Optional<AuthenticationToken> authenticateUser(Credential credential) throws EmailUnregisteredException;
}
