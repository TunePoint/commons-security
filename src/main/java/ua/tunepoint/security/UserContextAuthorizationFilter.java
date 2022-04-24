package ua.tunepoint.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.tunepoint.security.UserContextHeaders.USER_CONTEXT;

@Slf4j
@RequiredArgsConstructor
public class UserContextAuthorizationFilter extends OncePerRequestFilter {

    private final UserEncoder userEncoder;
    private final UserViewConverter converter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserContext.clear();

        try {
            var contextHeaderValue = request.getHeader(USER_CONTEXT);
            if (StringUtils.hasText(contextHeaderValue)) {

                var user = converter.convert(
                        userEncoder.decode(contextHeaderValue)
                );

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, user.getUsername(), user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

                UserContext.setHeader(contextHeaderValue);
            }
        } catch (Exception ex) {
            log.error("Error occurred while extracting user context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
