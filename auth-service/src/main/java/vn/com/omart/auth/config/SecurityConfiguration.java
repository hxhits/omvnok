package vn.com.omart.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import vn.com.omart.auth.filter.JsonToUrlEncodedAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JsonToUrlEncodedAuthenticationFilter jsonFilter;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new StandardPasswordEncoder();
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
            .userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoder())
        ;
    }



    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
            .addFilterBefore(jsonFilter, ChannelProcessingFilter.class) // parse json -> www-form for /oauth/token
            .authorizeRequests()
//        .antMatchers(actuatorEndpoints).permitAll()
            .antMatchers("/info").permitAll()
            .antMatchers("/health").permitAll()
            .antMatchers("/users/**").permitAll()
            .antMatchers("/accounts/**").permitAll()
            .antMatchers("/_internal/**").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
        ;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

