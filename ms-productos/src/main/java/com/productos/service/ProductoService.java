package com.productos.service;

import java.util.List;

import com.productos.dto.request.ProductoRequest;
import com.productos.dto.response.ProductoResponse;

public interface ProductoService {

    ProductoResponse create(ProductoRequest request);

    ProductoResponse getById(Long id);

    List<ProductoResponse> getAll();

    ProductoResponse update(Long id, ProductoRequest request);

    void delete(Long id);
}