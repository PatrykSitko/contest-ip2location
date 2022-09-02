package be.patryksitko.contest.ip2location.com.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.helpers.BCryptPasswordEncoder;
import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.repositoryDAO.CredentialRepository;
import be.patryksitko.contest.ip2location.com.repositoryDAO.UserRepository;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.EmailUnregisteredException;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public User registerUser(User user) throws EmailRegisteredException, IllegalArgumentException {
        if (userRepository.findByEmail(user.getCredential().getEmail()) != null) {
            throw new EmailRegisteredException(user.getCredential().getEmail());
        }
        user.getCredential().setPassword(BCryptPasswordEncoder.getInstance.encode(user.getCredential().getPassword()));
        credentialRepository.save(user.getCredential());
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) throws EmailUnregisteredException {
        final User user = userRepository.findByEmail(email);
        if (user instanceof User) {
            return user;
        }
        throw new EmailUnregisteredException(email);
    }
}
