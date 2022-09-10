package be.patryksitko.contest.ip2location.com.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import be.patryksitko.contest.ip2location.com.builders.ResponseBuilder;
import be.patryksitko.contest.ip2location.com.builders.ResponseBuilder.ResponseType;
import be.patryksitko.contest.ip2location.com.helpers.BCryptPasswordEncoder;
import be.patryksitko.contest.ip2location.com.helpers.exception.PasswordFormatException;
import be.patryksitko.contest.ip2location.com.model.AuthenticationToken;
import be.patryksitko.contest.ip2location.com.model.Credential;
import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.service.AuthenticationTokenService;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.EmailUnregisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.PasswordMismatchException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @PutMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> registerUser(@RequestBody @Valid User user) {
        try {
            if (BCryptPasswordEncoder.isPasswordMeetingStandards(user.getCredential().getPassword())) {
                userService.registerUser(user);
            }
        } catch (EmailRegisteredException | PasswordFormatException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseBuilder.builder().status(HttpStatus.CONFLICT).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(user.toJSON())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder().status(HttpStatus.CREATED)
                .responseType(ResponseType.SUCCESS).body(user.toJSON()).build());

    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> loginUser(@RequestBody @Valid Credential credential) {
        AuthenticationToken authenticationToken;
        try {
            authenticationToken = userService.authenticateUser(credential);
        } catch (EmailUnregisteredException e) {
            log.error(e.getMessage());
            credential.setFingerprint("hidden.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseBuilder.builder().status(HttpStatus.NOT_FOUND).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(credential.toJSON()).build());
        } catch (PasswordMismatchException e) {
            log.error(e.getMessage());
            credential.setFingerprint("hidden.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseBuilder.builder().status(HttpStatus.CONFLICT).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(credential.toJSON()).build());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(ResponseBuilder.builder().status(HttpStatus.FOUND).responseType(ResponseType.SUCCESS)
                        .body(authenticationToken.toJSON()).build());
    }

    @PostMapping(value = "/validate-authentication-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> validateAuthenticationToken(
            @RequestBody @Valid AuthenticationToken authenticationToken) {
        final boolean authenticationTokenIsValid = authenticationTokenService
                .validateAuthenticationToken(authenticationToken);
        final HttpStatus statusOfHttp = authenticationTokenIsValid ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        final JSONObject responseBody = new JSONObject();
        responseBody.put("isProvidedAuthenticationTokenValid", authenticationTokenIsValid);
        return ResponseEntity.status(statusOfHttp)
                .body(ResponseBuilder.builder().status(statusOfHttp)
                        .responseType(ResponseType.SUCCESS)
                        .body(responseBody.toString()).build());
    }
}
