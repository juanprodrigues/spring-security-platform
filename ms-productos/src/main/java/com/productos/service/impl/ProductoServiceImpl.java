package com.productos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.dto.request.ProductoRequest;
import com.productos.dto.response.ProductoResponse;
import com.productos.entity.Producto;
import com.productos.mapper.ProductoMapper;
import com.productos.repository.ProductoRepository;
import com.productos.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    @Override
    public ProductoResponse create(ProductoRequest request) {
        Producto producto = ProductoMapper.toEntity(request);
        return ProductoMapper.toResponse(repository.save(producto));
    }

    @Override
    public ProductoResponse getById(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return ProductoMapper.toResponse(producto);
    }

    @Override
    public List<ProductoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public ProductoResponse update(Long id, ProductoRequest request) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setActivo(request.activo());

        return ProductoMapper.toResponse(repository.save(producto));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}