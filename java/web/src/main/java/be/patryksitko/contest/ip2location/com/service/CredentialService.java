package be.patryksitko.contest.ip2location.com.service;

import javax.security.auth.login.CredentialException;

import be.patryksitko.contest.ip2location.com.model.Credential;

public interface CredentialService {

    public Credential saveCredential(Credential credential) throws CredentialException;

}