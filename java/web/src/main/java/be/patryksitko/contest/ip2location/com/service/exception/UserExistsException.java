package be.patryksitko.contest.ip2location.com.service.exception;

public class UserExistsException extends RuntimeException {

    public UserExistsException(String email) {
        super("[EMAIL]:The email \"" + email + "\" is already registered.");
    }
}
