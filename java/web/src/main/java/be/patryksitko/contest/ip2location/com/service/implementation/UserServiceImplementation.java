package be.patryksitko.contest.ip2location.com.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.repositoryDAO.CredentialRepository;
import be.patryksitko.contest.ip2location.com.repositoryDAO.UserRepository;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;

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
        credentialRepository.save(user.getCredential());
        return userRepository.save(user);
    }
}
