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
    public User registerUser(User user) throws UserExistsException, IllegalArgumentException {
        if (userRepository.findByEmail(user.getCredential().getEmail()) != null) {
            throw new UserExistsException(user.getCredential().getEmail());
        }
        return userRepository.save(user);
    }
}
