package ua.tunepoint.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BaseUserEncoder implements UserEncoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(UserView user) {
        try {
            return Base64.getEncoder()
                    .encodeToString(
                            mapper.writeValueAsString(user).getBytes(StandardCharsets.UTF_8)
                    );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public UserView decode(String encoded) {
        try {
            return mapper.readValue(
                    Base64.getDecoder().decode(encoded),
                    UserView.class
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
