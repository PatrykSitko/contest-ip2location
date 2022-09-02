package be.patryksitko.contest.ip2location.com.helpers.exception;

public class PasswordFormatException extends RuntimeException {

    public PasswordFormatException(String message) {
        super("[PASSWORD]:" + message);
    }

}
