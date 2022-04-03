package ua.tunepoint.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtAuthorizationRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (template != null && !JwtContext.isEmpty()) {
            template.header(AUTHORIZATION, JwtContext.getHeader());
        }
    }
}
