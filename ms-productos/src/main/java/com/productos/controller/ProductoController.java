package com.productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.dto.request.ProductoRequest;
import com.productos.dto.response.ProductoResponse;
import com.productos.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    @PostMapping
    public ProductoResponse create(@RequestBody @Valid ProductoRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ProductoResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ProductoResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ProductoResponse update(
            @PathVariable Long id,
            @RequestBody @Valid ProductoRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}