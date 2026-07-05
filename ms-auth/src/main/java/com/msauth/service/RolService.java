package com.msauth.service;

import com.msauth.entity.Rol;

public interface RolService {

    Rol findByNombre(String nombre);
}