package vn.com.omart.messenger.port.support.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    RemoteTokenServices tokenService;

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenService);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().authorizeRequests()
            .anyRequest().permitAll()
        // .requestMatchers().antMatchers("/foos/**","/bars/**")
        // .and()
        // .authorizeRequests()
        // .antMatchers(HttpMethod.GET,"/foos/**").access("#oauth2.hasScope('foo')
        // and #oauth2.hasScope('read')")
        // .antMatchers(HttpMethod.POST,"/foos/**").access("#oauth2.hasScope('foo')
        // and #oauth2.hasScope('write')")
        // .antMatchers(HttpMethod.GET,"/bars/**").access("#oauth2.hasScope('bar')
        // and #oauth2.hasScope('read')")
        // .antMatchers(HttpMethod.POST,"/bars/**").access("#oauth2.hasScope('bar')
        // and #oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
        ;
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenService(@Value("${auth.checkTokenEndpointUrl}") String checkTokenEndpointUrl,
                                            @Value("${auth.clientId}") String clientId,
                                            @Value("${auth.clientSecret}") String clientSecret) {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        return tokenService;
    }

}
