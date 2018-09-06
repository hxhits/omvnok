package vn.com.omart.auth.application;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import vn.com.omart.auth.domain.Role;
import vn.com.omart.auth.domain.User;
import vn.com.omart.auth.domain.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class CustomTokenEnhancer implements TokenEnhancer {

    private final UserRepository userRepository;

    public CustomTokenEnhancer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        String lowercaseLogin = authentication.getName().toLowerCase();

        User userFromDatabase;
        if (lowercaseLogin.contains("@")) {
            userFromDatabase = userRepository.findByEmail(lowercaseLogin);
        } else {
            userFromDatabase = userRepository.findByUsernameCaseInsensitive(lowercaseLogin);
        }

        final Map<String, Object> additionalInfo = new HashMap<>();
//        additionalInfo.put("organization", authentication.getName() + randomAlphabetic(4));
        additionalInfo.put("fullname", userFromDatabase.getFirstname());
        additionalInfo.put("user_id", userFromDatabase.getId());
        additionalInfo.put("title", userFromDatabase.getTitle());
        additionalInfo.put("roles", userFromDatabase.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        userFromDatabase.getRoles().forEach(role -> {
            List<SimpleGrantedAuthority> collect = role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .collect(Collectors.toList());
            grantedAuthorities.addAll(collect);
        });

        additionalInfo.put("permissions", grantedAuthorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList()));

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;

    }

}
