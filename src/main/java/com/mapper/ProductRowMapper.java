package com.mapper;

import com.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        var product = new Product();
        product.setProduct_id(rs.getLong("product_id"));
        product.setProduct_name(rs.getString("product_name"));
        product.setProduct_price(rs.getBigDecimal("product_price"));
        product.setProduct_origin(rs.getString("product_origin"));

        return product;
    }
}
