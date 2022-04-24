package ua.tunepoint.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AlgorithmProvider {

    public static Algorithm get(String secret) {
        return Algorithm.HMAC256(secret.getBytes());
    }
}
