package ua.tunepoint.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContextHeaders {

    public static final String USER_CONTEXT = "X-User-Context";
    public static final String REQUEST_CORRELATION_ID = "X-Correlation-Id";
}
