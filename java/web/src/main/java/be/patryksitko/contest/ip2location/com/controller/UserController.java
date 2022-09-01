package be.patryksitko.contest.ip2location.com.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import be.patryksitko.contest.ip2location.com.builders.ResponseBuilder;
import be.patryksitko.contest.ip2location.com.builders.ResponseBuilder.ResponseType;
import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> registerUser(@RequestBody @Valid User user) {
        System.out.println(user.toJSON());
        try {
            userService.registerUser(user);
        } catch (EmailRegisteredException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseBuilder.builder().status(HttpStatus.CONFLICT).errors(List.of(e.getMessage()))
                            .responseType(ResponseType.ERROR).body(user.toJSON())
                            .build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder().status(HttpStatus.CREATED)
                .responseType(ResponseType.SUCCESS).body(user.toJSON()).build());

    }
}
