package com.bo;

import com.dao.ProductRepository;
import com.dao.ProductRepositoryImpl;
import com.dao.TimestampsRepository;
import com.exception.NonHalalException;
import com.mapper.ProductMapper;
import com.model.RawProduct;
import com.model.Timestamps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductProcessor {

    private final ProductRepository productRepository;
    private final TimestampsRepository timestampsRepository;

    @Autowired
    public ProductProcessor(ProductRepository productRepository, TimestampsRepository timestampsRepository){
        this.productRepository = productRepository;
        this.timestampsRepository = timestampsRepository;
    }

    @Transactional
    public void insertProducts(List<RawProduct> rawProducts){
        var timestamp = new Timestamps();

        if(rawProducts.size() == 1){
            var product = ProductMapper.INSTANCE.mapRawProductToProductDto(rawProducts.get(0));
            timestamp.setInserted_by("Maki");
            timestamp.setProduct_name(product.getProduct_name());
            timestamp.setTiming(new Time(System.currentTimeMillis()));
            timestampsRepository.insertTimestamp(timestamp);

            if(product.getProduct_name().equalsIgnoreCase("Pork")){
                throw new NonHalalException("No pork can be inserted into this database!!!");
            }

            productRepository.insertAllProducts(List.of(product));

        } else {
            var concatProductString = rawProducts.stream()
                    .map(RawProduct::getProductName)
                    .collect(Collectors.joining(", "));

            timestamp.setInserted_by("Maki");
            timestamp.setProduct_name(concatProductString);
            timestamp.setTiming(new Time(System.currentTimeMillis()));
            timestampsRepository.insertTimestamp(timestamp);

            {
                var hasPork = rawProducts.stream()
                        .filter(v -> v.getProductName().equalsIgnoreCase("Pork"))
                        .findAny();

                if(hasPork.isPresent()){
                    throw new NonHalalException("No pork can be inserted into this database!!!");
                }
            }

            var products = rawProducts.stream()
                    .map(ProductMapper.INSTANCE::mapRawProductToProductDto)
                    .collect(Collectors.toList());

            productRepository.insertAllProducts(products);
        }

    }

    @Transactional
    public List<RawProduct> getAllProducts(int page, int size){
        return productRepository.getAllProducts(page, size)
                .stream()
                .map(ProductMapper.INSTANCE::mapProductToRawProductDto)
                .collect(Collectors.toList());
    }

}
