package com.techopact.store.service;

import com.techopact.store.entities.AuthenticationRequest;

public interface RegistrationService {
    boolean saveUserDetails(AuthenticationRequest authReq);
}
