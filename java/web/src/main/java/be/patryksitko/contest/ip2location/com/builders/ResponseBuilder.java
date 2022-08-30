package be.patryksitko.contest.ip2location.com.builders;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ResponseBuilder {

    private HttpStatus status;
    private ResponseType type;
    private List<String> errors;
    private String body;

    public static enum ResponseType {
        ERROR, SUCCESS;

    }
}
