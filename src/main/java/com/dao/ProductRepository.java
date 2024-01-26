package com.dao;

import com.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository {
    Boolean checkIfExist(long id);
    List<Product> getAllProducts(Integer page, Integer size);
    void insertAllProducts(List<Product> products);
    void updateProduct(long id, String name, BigDecimal price, String origin);
}
