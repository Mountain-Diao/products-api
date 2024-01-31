package com.dao;

import com.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MysqlProductsRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameIn(List<String> name);

    List<Product> findByProductId(Long id);

    Page<Product> findAll(Pageable pageable);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Product p SET " +
//            "p.productName = COALESCE(p.productName, :product_name), " +
//            "p.productPrice = COALESCE(p.productPrice, :product_price), " +
//            "p.productOrigin = COALESCE(p.productOrigin, :product_origin) " +
//            "WHERE p.productId = :product_id")
//    int updateProduct(@Param("product_id") Long productId,
//                      @Param("product_name") String productName,
//                      @Param("product_price") BigDecimal productPrice,
//                      @Param("product_origin") String productOrigin);

    @Transactional
    @Modifying
    @Query("UPDATE Product SET product_code = :productCode, product_name = :productName, " +
            "product_price = :productPrice, product_origin = :productOrigin, product_category = :productCategory, " +
            "product_brand = :productBrand, product_description = :productDescription WHERE product_id = :productId")
    int updateProduct(@Param("productId") Long productId,
                      @Param("productCode") String productCode,
                      @Param("productName") String productName,
                      @Param("productPrice") BigDecimal productPrice,
                      @Param("productOrigin") String productOrigin,
                      @Param("productCategory") String productCategory,
                      @Param("productBrand") String productBrand,
                      @Param("productDescription") String productDescription);
}
