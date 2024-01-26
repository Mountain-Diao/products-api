package com.controller;

import com.bo.ProductProcessor;
import com.dao.ExternalApiDao;
import com.dao.ProductRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.RawProduct;
import com.model.ResponseEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private static final String MESSAGE = "message";
    private final ProductProcessor processor;
    private final ProductRepositoryImpl repository;
    private final ExternalApiDao externalApiDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    public ApiController(ProductProcessor processor, ProductRepositoryImpl repository, ExternalApiDao externalApiDao){
        this.processor = processor;
        this.repository = repository;
        this.externalApiDao = externalApiDao;
    }

    @GetMapping("/products/has-orders")
    public Boolean getProducts(@RequestParam long id){
        try{
            return externalApiDao.callOrdersApi(id);
        } catch (IOException e){
            logger.error("Unable to call Orders API!");
            return false;
        }

    }

    @GetMapping("/products/page")
    public ResponseEntity<Object> getAllProducts(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        logger.trace("ENDPOINT CALLED: /products/page");
        logger.trace("Input params: page = {}, size = {}", page, size);

        if( page < 1 || size < 1 ){
            logger.trace("Neither page or size can be 0! Endpoint returned response with http status = {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
            headers.add(MESSAGE, "Neither page or size can be 0!");

            return new ResponseEntity<>(new ResponseEnvelope(
                    "Neither page or size can be 0!", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    headers, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            logger.trace("Successfully retrieve products on page = {} with size = {} with http status = {}",page, size, HttpStatus.OK.value());
            headers.add(MESSAGE, "Product(s) retrieved successfully");

            return new ResponseEntity<>(new ResponseEnvelope(
                    "Product(s) retrieved successfully", HttpStatus.OK.value(), processor.getAllProducts(page, size)),
                    headers, HttpStatus.OK.value());
        }
    }

    @PutMapping(value = "/products/update",
            produces = { "application/json" }
    )
    public ResponseEntity<Object> updateProduct(@RequestParam long productId, @RequestParam String productName,
                                                @RequestParam BigDecimal productPrice, @RequestParam String productOrigin){
        logger.trace("ENDPOINT CALLED: /products/update");
        logger.trace("Input params: productId = {}, productName = {}, productPrice = {}, productOrigin = {}", productId, productName, productPrice, productOrigin);
        var isExists = repository.checkIfExist(productId);


        if(isExists){
            repository.updateProduct(productId, productName, productPrice, productOrigin);
            headers.add(MESSAGE, String.format("Product with ID: %s updated successfully", productId));

            logger.trace("Product with ID = {} updated successfully with http status = {}", productId, HttpStatus.OK.value());

            return new ResponseEntity<>(new ResponseEnvelope(
                    String.format("Product with ID: %s updated successfully", productId), HttpStatus.OK.value()),
                    headers, HttpStatus.OK.value());
        } else {
            headers.add(MESSAGE, String.format("Product with ID = %s does not exists", productId));

            logger.trace("Product with ID = {} was not updated with http status = {}", productId, HttpStatus.NOT_MODIFIED.value());

            return new ResponseEntity<>(new ResponseEnvelope(
                    String.format("Product with ID = %s does not exists", productId), HttpStatus.NOT_MODIFIED.value()),
                    headers, HttpStatus.NOT_MODIFIED.value());
        }


    }

    @PostMapping("/insert")
    public ResponseEntity<String> postProducts(@RequestBody List<RawProduct> rawProducts){
        String rawProductJson = "";

        try{
            rawProductJson = objectMapper.writeValueAsString(rawProducts);
        } catch (JsonProcessingException e){
            logger.error("Unable to convert List of RawProducts to JSON", e);
        }

        logger.trace("ENDPOINT CALLED: /insert");
        logger.trace("Input body: {}", rawProductJson);

        processor.insertProducts(rawProducts);

        logger.trace("Product(s) was(were) inserted successfully with http status = {}", HttpStatus.CREATED.value());

        return new ResponseEntity<>("Products inserted successfully", HttpStatus.CREATED);
    }
}
