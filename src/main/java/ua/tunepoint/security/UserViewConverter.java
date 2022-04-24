package ua.tunepoint.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

public class UserViewConverter {

    public UserPrincipal convert(UserView view) {
        return UserPrincipal.builder()
                .id(view.getId())
                .username(view.getUsername())
                .authorities(
                        view.getAuthorities()
                                .stream().map(
                                        SimpleGrantedAuthority::new
                                ).collect(
                                        Collectors.toSet()
                                )
                )
                .build();
    }

    public UserView convert(UserPrincipal user) {
        return UserView.builder()
                .id(user.getId())
                .username(user.getUsername())
                .authorities(
                        user.getAuthorities()
                                .stream().map(
                                        GrantedAuthority::getAuthority
                                ).collect(
                                        Collectors.toSet()
                                )
                )
                .build();
    }
}
