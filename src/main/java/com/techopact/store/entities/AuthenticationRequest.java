package com.techopact.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_DETAILS")
public class AuthenticationRequest implements Serializable {
    @Id
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String username;
    private String password;

}
