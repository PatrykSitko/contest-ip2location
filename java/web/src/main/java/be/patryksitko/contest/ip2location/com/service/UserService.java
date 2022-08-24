package be.patryksitko.contest.ip2location.com.service;

import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.service.exception.UserExistsException;

public interface UserService {

    public User createUser(User user) throws UserExistsException;
}
