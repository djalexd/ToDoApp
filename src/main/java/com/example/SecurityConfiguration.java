package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService detailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.anyRequest().anonymous();
	}

	@Component
    public static class MyUserDetailsService implements UserDetailsService {
        @Autowired
        private UserRepository repository;

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            log.info("Looking for a user: {}", s);
            final com.example.domain.User ourUser = repository.findByUserName(s);
            if (ourUser == null) {
                throw new UsernameNotFoundException("We don't have user with userName " + s);
            }
            return new org.springframework.security.core.userdetails.User(
                    Long.toString(ourUser.getId()),
                    ourUser.getPassword(),
                    Collections.emptyList());
        }
    }
}
