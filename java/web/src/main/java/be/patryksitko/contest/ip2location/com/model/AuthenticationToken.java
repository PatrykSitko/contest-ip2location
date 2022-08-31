package be.patryksitko.contest.ip2location.com.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@JsonCreator), access = AccessLevel.PUBLIC)
@JsonRootName(value = "authenticationToken", namespace = "authenticationTokens")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "fingerprint", "authenticationToken", "deviceType" })
@Entity
@Table(name = "authentication_tokens")
public class AuthenticationToken implements Serializable, Cloneable {

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    public static enum DeviceType {
        IOS_APP, ANDROID_APP, BROWSER_APP_DESKTOP, BROWSER_APP_MOBILE, LINUX_APP, WINDOWS_APP, OTHER;
    }

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NonNull
    @ManyToOne
    @JsonProperty("credential")
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "credential_id", value = ConstraintMode.CONSTRAINT), referencedColumnName = "id")
    private Credential credential;

    @NonNull
    @JsonProperty("fingerpint")
    @Column(name = "user_fingerprint", nullable = false)
    private String userFingerprint;

    @NonNull
    @JsonProperty("authenticationToken")
    @Column(name = "user_authentication_token", nullable = false)
    private String userAuthenticationToken;

    @NonNull
    @JsonProperty("deviceType")
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    public String toJSON() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public AuthenticationToken clone() {
        try {
            return (AuthenticationToken) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
