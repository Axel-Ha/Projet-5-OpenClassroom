package com.openclassrooms.starterjwt;


import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Bean
    public AuthEntryPointJwt authEntryPointJwt(){
        return new AuthEntryPointJwt();
    }

}