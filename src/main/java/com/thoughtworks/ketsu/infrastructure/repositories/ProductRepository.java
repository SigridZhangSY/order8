package com.thoughtworks.ketsu.infrastructure.repositories;

import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.ProductMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductRepository implements com.thoughtworks.ketsu.domain.product.ProductRepository {
    @Inject
    ProductMapper productMapper;

    @Override
    public Optional<Product> createProduct(Map<String, Object> info) {
        productMapper.save(info);
        return Optional.ofNullable(productMapper.findById(Long.valueOf(String.valueOf(info.get("id")))));
    }

    @Override
    public List<Product> listProducts() {
        return productMapper.getAll();
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.ofNullable(productMapper.findById(id));
    }
}
