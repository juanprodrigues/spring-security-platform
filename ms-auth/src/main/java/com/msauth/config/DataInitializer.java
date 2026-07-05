package com.msauth.config;

import com.msauth.entity.Rol;
import com.msauth.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(
            RolRepository rolRepository
    ) {

        return args -> {

            if (rolRepository.findByNombre(
                    "ROLE_ADMIN"
            ).isEmpty()) {

                rolRepository.save(
                        Rol.builder()
                                .nombre("ROLE_ADMIN")
                                .build()
                );
            }

            if (rolRepository.findByNombre(
                    "ROLE_USER"
            ).isEmpty()) {

                rolRepository.save(
                        Rol.builder()
                                .nombre("ROLE_USER")
                                .build()
                );
            }
        };
    }
}