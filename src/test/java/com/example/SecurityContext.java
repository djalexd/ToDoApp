package com.example;

import com.example.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {
	
	public static <T> T executeInUserContext(User user, Callback<T> callback) {
		try {
			final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					user.getUserName(), user.getPassword());
			SecurityContextHolder.getContext().setAuthentication(token);
			return callback.executeInContext(user);
		} finally {
			SecurityContextHolder.clearContext();
		}
	}
	
	static interface Callback<T> {
		T executeInContext(User user);
	}
}
