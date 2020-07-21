package com.techopact.store.repository;

import com.techopact.store.entities.AuthenticationRequest;
import org.springframework.data.repository.CrudRepository;


public interface RegistrationRepository extends CrudRepository<AuthenticationRequest, String> {
}
