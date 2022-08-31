package be.patryksitko.contest.ip2location.com.service.implementation;

import javax.security.auth.login.CredentialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.model.Credential;
import be.patryksitko.contest.ip2location.com.repositoryDAO.CredentialRepository;
import be.patryksitko.contest.ip2location.com.service.CredentialService;

@Service
public class CredentialServiceImplementation implements CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public Credential saveCredential(Credential credential) throws CredentialException {
        return credentialRepository.save(credential);
    }
}
