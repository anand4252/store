package com.techopact.store.service.impl;

import com.techopact.store.entities.AuthenticationRequest;
import com.techopact.store.repository.RegistrationRepository;
import com.techopact.store.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    private final RegistrationRepository registrationRepository;

    @Override
    public boolean saveUserDetails(AuthenticationRequest authReq) {
        try {
            registrationRepository.save(authReq);
            return true;
        } catch (Exception e) {
            logger.debug("Exception occurred while saving the user details.{e}", e);
            return false;
        }
    }
}
