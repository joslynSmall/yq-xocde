package com.yq.xcode.security.oauth.yq;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class YqDaoAuthenticationProvider extends  AbstractUserDetailsAuthenticationProvider{

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private String userNotFoundEncodedPassword;
    
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		System.out.println("authentication.getCredentials() "+authentication.getCredentials());
		if (authentication.getCredentials() == null) {
			log.debug("Authentication failed: no credentials provided");
			throw new BadCredentialsException("AbstractUserDetailsAuthenticationProvider.badCredentials");
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(userDetails.getPassword(), presentedPassword)) {
			log.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException("AbstractUserDetailsAuthenticationProvider.badCredentials");
		} 
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
        UserDetails loadedUser;
        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {
            if(authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                passwordEncoder.matches(userNotFoundEncodedPassword, presentedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
}
