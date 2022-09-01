package be.patryksitko.contest.ip2location.com.service.implementation;

import javax.security.auth.login.CredentialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.model.Credential;
import be.patryksitko.contest.ip2location.com.repositoryDAO.CredentialRepository;
import be.patryksitko.contest.ip2location.com.service.CredentialService;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;

@Service
public class CredentialServiceImplementation implements CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public Credential saveCredential(Credential credential) throws CredentialException {
        if (credentialRepository.findByEmail(credential.getEmail()) != null) {
            throw new EmailRegisteredException(credential.getEmail());
        }
        return credentialRepository.save(credential);
    }
}
