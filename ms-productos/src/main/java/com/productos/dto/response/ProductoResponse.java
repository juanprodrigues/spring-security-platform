package com.productos.dto.response;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String nombre,
        BigDecimal precio,
        Integer stock,
        Boolean activo
) {}