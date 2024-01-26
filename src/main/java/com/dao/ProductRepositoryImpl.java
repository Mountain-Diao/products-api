package com.dao;

import com.controller.ApiController;
import com.mapper.ProductRowMapper;
import com.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository{
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Boolean checkIfExist(long id){
        String sql = "SELECT * FROm products WHERE product_id = ?";
        try {
            var product = jdbcTemplate.queryForObject(sql, new Object[]{id}, new ProductRowMapper());
            return true;
        } catch (EmptyResultDataAccessException e){
            logger.error("There is no such product with id = {}", id);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> getAllProducts(Integer page, Integer size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql,
                new Object[]{size, offset},
                (rs, rowNum) -> {
                    Product product = new Product();
                    product.setProduct_id(rs.getLong("product_id"));
                    product.setProduct_name(rs.getString("product_name"));
                    product.setProduct_price(rs.getBigDecimal("product_price"));
                    product.setProduct_origin(rs.getString("product_origin"));
                    return product;
                }
        );
    }

    @Override
    public void insertAllProducts(List<Product> products) {
        String sql = "INSERT INTO products (product_name, product_price, product_origin) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Product product = products.get(i);
                preparedStatement.setString(1, product.getProduct_name());
                preparedStatement.setBigDecimal(2, product.getProduct_price());
                preparedStatement.setString(3, product.getProduct_origin());
            }

            @Override
            public int getBatchSize() {
                return products.size();
            }
        });
    }

    @Override
    public void updateProduct(long id, String name, BigDecimal price, String origin){
        String sql = "UPDATE products SET product_name = ?, product_price = ?, product_origin = ? WHERE product_id = ?";
        jdbcTemplate.update(sql, name, price, origin, id);
    }
}
