package com.msauth.service.impl;

import com.msauth.entity.Rol;
import com.msauth.exception.RoleNotFoundException;
import com.msauth.repository.RolRepository;
import com.msauth.service.RolService;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl
        implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(
            RolRepository rolRepository
    ) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol findByNombre(
            String nombre
    ) {

        return rolRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Rol no encontrado: " + nombre
                        )
                );
    }
}