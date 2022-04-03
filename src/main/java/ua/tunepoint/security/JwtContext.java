package ua.tunepoint.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtContext {

    private static final ThreadLocal<String> headerHolder = new InheritableThreadLocal<>();

    public static void setHeader(String header) {
        headerHolder.set(header);
    }

    public static String getHeader() {
        return headerHolder.get();
    }

    public static void clear() {
        headerHolder.remove();
    }

    public static boolean isEmpty() {
        return headerHolder.get() == null;
    }
}
