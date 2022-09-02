package be.patryksitko.contest.ip2location.com.service;

import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.EmailUnregisteredException;

public interface UserService {

    public User registerUser(User user) throws EmailRegisteredException;

    public User findUserByEmail(String email) throws EmailUnregisteredException;
}
