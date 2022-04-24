package ua.tunepoint.security;

public interface UserEncoder {

    String encode(UserView user);

    UserView decode(String encoded);
}
