package com.mapper;

import com.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        var product = new Product();
        product.setProductId(rs.getLong("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setProductPrice(rs.getBigDecimal("product_price"));
        product.setProductOrigin(rs.getString("product_origin"));

        return product;
    }
}
