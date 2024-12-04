package com.gym.crm.application.dto.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationInfo {

    private String username;

    @JsonIgnore
    @Transient
    @ToString.Exclude
    private String password;

}
