package com.dao;

import com.controller.ApiController;
import com.model.Timestamps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public class TimestampsRepositoryImpl implements TimestampsRepository{
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    public TimestampsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertTimestamp(Timestamps timestamps) {
        String sql = "INSERT INTO timestamps (inserted_by, product_name, timing) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, timestamps.getInserted_by(), timestamps.getProduct_name(), timestamps.getTiming());
        logger.trace("Query executed: {}", sql);

    }
}
