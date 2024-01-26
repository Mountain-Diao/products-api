package com.bo;

import com.dao.ProductRepositoryImpl;
import com.mapper.ProductMapper;
import com.model.RawProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductProcessor {

    private final ProductRepositoryImpl repository;

    @Autowired
    public ProductProcessor(ProductRepositoryImpl repository){
        this.repository = repository;
    }

    @Transactional
    public void insertProducts(List<RawProduct> rawProducts){
        var products = rawProducts.stream()
                .map(ProductMapper.INSTANCE::mapRawProductToProductDto)
                .collect(Collectors.toList());
        repository.insertAllProducts(products);
    }

    @Transactional
    public List<RawProduct> getAllProducts(int page, int size){
        return repository.getAllProducts(page, size)
                .stream()
                .map(ProductMapper.INSTANCE::mapProductToRawProductDto)
                .collect(Collectors.toList());
    }

}
