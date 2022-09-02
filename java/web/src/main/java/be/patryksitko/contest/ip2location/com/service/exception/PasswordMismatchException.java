package be.patryksitko.contest.ip2location.com.service.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException(String email) {
        super("The given password doesn't correspond to \"" + email + "\".");
    }
}
