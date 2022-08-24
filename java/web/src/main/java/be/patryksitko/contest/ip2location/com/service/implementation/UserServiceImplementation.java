package be.patryksitko.contest.ip2location.com.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.repository.UserRepository;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.UserCreatedException;
import be.patryksitko.contest.ip2location.com.service.exception.UserExistsException;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) {
        final List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.contains(user)) {
            throw new UserExistsException(user.getEmail());
        }
        userRepository.save(user);
        throw new UserCreatedException(user.getEmail());
    }
}
