package be.patryksitko.contest.ip2location.com.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @GetMapping(value = "/get-ip", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> registerUser(HttpServletRequest request) {
        final JSONObject responseBody = new JSONObject();
        responseBody.put("client-ip", request.getRemoteAddr());
        return ResponseEntity.ok(responseBody.toString());
    }
}
