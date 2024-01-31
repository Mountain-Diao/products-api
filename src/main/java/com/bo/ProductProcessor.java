package com.bo;

import com.dao.MysqlProductsRepository;
import com.dao.MysqlTimestampsRepository;
import com.exception.NonHalalException;
import com.mapper.ProductMapper;
import com.model.Product;
import com.model.RawProduct;
import com.model.Timestamps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductProcessor {

    private final MysqlProductsRepository mysqlProductsRepository;
    private final MysqlTimestampsRepository mysqlTimestampsRepository;

    @Autowired
    public ProductProcessor(MysqlProductsRepository mysqlProductsRepository, MysqlTimestampsRepository mysqlTimestampsRepository){
        this.mysqlProductsRepository = mysqlProductsRepository;
        this.mysqlTimestampsRepository = mysqlTimestampsRepository;
    }

    @Transactional
    public void insertProducts(List<RawProduct> rawProducts){
        var timestamp = new Timestamps();

        if(rawProducts.size() == 1){
            var product = ProductMapper.INSTANCE.mapRawProductToProductDto(rawProducts.get(0));
            timestamp.setInserted_by("Maki");
            timestamp.setProduct_name(product.getProductName());
            timestamp.setTiming(new Time(System.currentTimeMillis()));
            mysqlTimestampsRepository.save(timestamp);

            if(product.getProductName().equalsIgnoreCase("Pork")){
                throw new NonHalalException("No pork can be inserted into this database!!!");
            }

            mysqlProductsRepository.save(product);

        } else {
            var concatProductString = rawProducts.stream()
                    .map(RawProduct::getProductName)
                    .collect(Collectors.joining(", "));

            timestamp.setInserted_by("Maki");
            timestamp.setProduct_name(concatProductString);
            timestamp.setTiming(new Time(System.currentTimeMillis()));
            mysqlTimestampsRepository.save(timestamp);

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

            mysqlProductsRepository.saveAll(products);
        }

    }

    @Transactional
    public Page<Product> getAllProducts(int page, int size){
        var pageable = PageRequest.of(page - 1 , size);
        return mysqlProductsRepository.findAll(pageable);
    }

}
