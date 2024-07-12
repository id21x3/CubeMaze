package sk.tuke.backend.gamestudio.server.service.securtity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UsernameJwtAuthenticationToken extends AbstractAuthenticationToken {

    UserDetails principal;

    public UsernameJwtAuthenticationToken(UserDetails principal) {
        super(principal.getAuthorities());
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}