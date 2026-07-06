package com.productos.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(

        @NotBlank
        String nombre,

        @NotNull
        @Positive
        BigDecimal precio,

        @NotNull
        @Min(0)
        Integer stock,

        Boolean activo
) {}