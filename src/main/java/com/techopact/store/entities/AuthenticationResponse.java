package com.techopact.store.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class AuthenticationResponse implements Serializable {
    private final String jwt;

}
