package be.patryksitko.contest.ip2location.com.service.exception;

public class UserCreatedException extends RuntimeException {

    public UserCreatedException(String email) {
        super("Registered new user under \"" + email + " email.");
    }
}
