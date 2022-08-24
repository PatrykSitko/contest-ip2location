package be.patryksitko.contest.ip2location.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AccessLevel;
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
// @AllArgsConstructor(onConstructor = @__(@JsonCreator), access =
// AccessLevel.PUBLIC)
@JsonRootName(value = "user", namespace = "users")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "firstname", "lastname", "email" })
@Entity
@Table(name = "users")
public class User implements Serializable {

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JacksonInject
    @Column(name = "id", nullable = false)
    private Long id;

    @NonNull
    @JsonProperty("firstname")
    @Column(name = "firstname", nullable = false)
    public String firstname;

    @NonNull
    @JsonProperty("lastname")
    @Column(name = "lastname", nullable = false)
    public String lastname;

    @NonNull
    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    public String email;

    @NonNull
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

    @JsonCreator
    public User(@JsonProperty("id") Long id, @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this(firstname, lastname, email, password);
        this.id = id;
    }
}
