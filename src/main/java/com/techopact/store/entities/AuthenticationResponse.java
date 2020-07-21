package com.techopact.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class AuthenticationResponse implements Serializable {
    private String jwt;

}
