package com.productos.mapper;

import com.productos.dto.request.ProductoRequest;
import com.productos.dto.response.ProductoResponse;
import com.productos.entity.Producto;

public class ProductoMapper {

    public static Producto toEntity(ProductoRequest req) {
        return Producto.builder()
                .nombre(req.nombre())
                .precio(req.precio())
                .stock(req.stock())
                .activo(req.activo() != null ? req.activo() : true)
                .build();
    }

    public static ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getActivo()
        );
    }
}