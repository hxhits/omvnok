package vn.com.omart.auth.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.omart.auth.domain.UserNotActivatedException;
import vn.com.omart.auth.domain.User;
import vn.com.omart.auth.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {

        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        User userFromDatabase;
        if (lowercaseLogin.contains("@")) {
            userFromDatabase = userRepository.findByEmail(lowercaseLogin);
        } else {
            userFromDatabase = userRepository.findByUsernameCaseInsensitive(lowercaseLogin);
        }

        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } else if (!userFromDatabase.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " is not activated");
        }

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        userFromDatabase.getRoles().forEach(role -> {
            List<SimpleGrantedAuthority> collect = role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getCode()))
                .collect(Collectors.toList());
            grantedAuthorities.addAll(collect);
        });

//        List<SimpleGrantedAuthority> grantedAuthorities =
//            userFromDatabase.getAuthorities().stream()
//            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            userFromDatabase.getUsername(), userFromDatabase.getPassword(),
            true, true, true, true,
            grantedAuthorities);

    }


}
