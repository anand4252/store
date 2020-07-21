package com.techopact.store.service.impl;

import com.techopact.store.entities.AuthenticationRequest;
import com.techopact.store.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StoreUserDetailsServiceImpl implements UserDetailsService {

	private final RegistrationRepository registrationRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Optional<AuthenticationRequest> userDetails = registrationRepository.findById(username);
		if(!userDetails.isPresent()){
			throw new UsernameNotFoundException("There is no such user");
		}
		return userDetails.map(e -> new  User(e.getUsername(), e.getPassword(), new ArrayList<>())).get();
	}

}
