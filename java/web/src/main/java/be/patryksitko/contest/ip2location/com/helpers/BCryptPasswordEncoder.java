package be.patryksitko.contest.ip2location.com.helpers;

public interface BCryptPasswordEncoder {

    static final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder getInstance = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
}
