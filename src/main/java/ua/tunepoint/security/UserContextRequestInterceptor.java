package ua.tunepoint.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class UserContextRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (template != null && !UserContext.isEmpty()) {
            template.header(UserContextHeaders.USER_CONTEXT, UserContext.getHeader());
        }
    }
}
