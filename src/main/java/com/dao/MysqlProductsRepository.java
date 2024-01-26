package com.dao;

import com.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MysqlProductsRepository extends JpaRepository<Product, Long> {

    List<Product> findProductPriceByProductName(List<String> name);

    void saveAll(List<Product> products);
}
