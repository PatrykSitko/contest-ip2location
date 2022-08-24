package be.patryksitko.contest.ip2location.com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@JsonCreator), access = AccessLevel.PUBLIC)
@JsonRootName(value = "user", namespace = "users")
@JsonPropertyOrder({ "id", "firstname", "lastname", "email" })
@Entity
@Table(name = "users")
public class User {

    @Id
    @NonNull
    @GeneratedValue
    @JacksonInject
    private Long id;

    @JsonProperty("firstname")
    @Column(name = "firstname", nullable = false)
    public String firstname;

    @JsonProperty("lastname")
    @Column(name = "lastname", nullable = false)
    public String lastname;

    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    public String email;

    @Getter(onMethod = @__(@JsonIgnore))
    @Setter(onMethod = @__(@JsonProperty("password")))
    @JsonAlias({ "password", "passwd" })
    @Column(name = "password", nullable = false)
    public String password;

    @JsonCreator
    public User(@JsonProperty("firstname") String firstname, @JsonProperty("lastname") String lastname,
            @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
