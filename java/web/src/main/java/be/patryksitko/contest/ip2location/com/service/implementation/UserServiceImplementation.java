package be.patryksitko.contest.ip2location.com.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.repositoryDAO.UserRepository;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.UserExistsException;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) throws UserExistsException, IllegalArgumentException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserExistsException(user.getEmail());
        }
        return userRepository.save(user);
    }
}