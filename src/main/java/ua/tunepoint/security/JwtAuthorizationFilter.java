package ua.tunepoint.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final Algorithm algorithm;

    public JwtAuthorizationFilter(SecurityProperties properties) {
        this.algorithm = Algorithm.HMAC256(properties.getSecret().getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        JwtContext.clear();

        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            String token = extractToken(authorizationHeader);

            var verifier = JWT.require(algorithm).build();

            var principal = decode(verifier.verify(token));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, principal.getUsername(), principal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);

            JwtContext.setHeader(authorizationHeader);
        }

        filterChain.doFilter(request, response);
    }

    private UserPrincipal decode(DecodedJWT jwt) {

        var authorities = jwt.getClaims().get("roles").asList(String.class).stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        var id = jwt.getClaims().get("id").asLong();

        return UserPrincipal.builder()
                .username(jwt.getSubject())
                .id(id)
                .authorities(authorities)
                .build();
    }

    private String extractToken(String headerValue) {
        return headerValue.substring(BEARER.length());
    }
}
