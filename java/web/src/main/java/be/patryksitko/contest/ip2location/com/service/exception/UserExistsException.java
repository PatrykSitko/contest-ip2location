package be.patryksitko.contest.ip2location.com.service.exception;

public class UserExistsException extends RuntimeException {

    public UserExistsException(String email) {
        super("The email \"" + email + "\" is already registered.");
    }
}
