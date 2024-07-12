package sk.tuke.backend.gamestudio.server.service.securtity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.tuke.backend.gamestudio.server.service.user.UserService;
import sk.tuke.backend.gamestudio.server.service.user.UserDetailsImpl;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TokenFilter extends OncePerRequestFilter {
    private JwtCore jwtCore;
    private UserService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);
        if (!jwtCore.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtCore.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsernameOptional(username)
                .map(UserDetailsImpl::new)
                .orElse(null);

        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = new UsernameJwtAuthenticationToken(userDetails);
        authentication.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}