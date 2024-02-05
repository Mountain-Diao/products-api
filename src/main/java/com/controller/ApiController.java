package com.controller;

import com.bo.ProductProcessor;
import com.dao.ExternalApiDao;
import com.dao.MysqlProductsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Product;
import com.model.RawProduct;
import com.model.ResponseEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private static final String MESSAGE = "message";
    private final ProductProcessor processor;
    private final MysqlProductsRepository mysqlProductsRepository;
    private final ExternalApiDao externalApiDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    public ApiController(ProductProcessor processor, MysqlProductsRepository mysqlProductsRepository, ExternalApiDao externalApiDao){
        this.processor = processor;
        this.mysqlProductsRepository = mysqlProductsRepository;
        this.externalApiDao = externalApiDao;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/products/has-orders")
    public ResponseEntity<Object> getProducts(@RequestParam long id){
        logger.trace("ENDPOINT CALLED: /products/has-orders");
        logger.trace("Input params: id = {}", id);
        try{
            var hasRow = externalApiDao.callOrdersApi(id);

            headers.add(MESSAGE, "Successfully checked!");

            return hasRow ? new ResponseEntity<>(new ResponseEnvelope(
                    "This product id has orders!", HttpStatus.OK.value()),
                    headers, HttpStatus.OK.value()) :
                    new ResponseEntity<>(new ResponseEnvelope(
                    "This product id has no orders!", HttpStatus.OK.value()),
                    headers, HttpStatus.OK.value());
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Unable to call Orders API!");
            return new ResponseEntity<>(new ResponseEnvelope(
                    "Unable to call orders API!", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    headers, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @CrossOrigin(origins = "*")
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

            return new ResponseEntity<>(processor.getAllProducts(page, size), headers, HttpStatus.OK.value());
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/product")
    public ResponseEntity<Object> getAllProduct(@RequestParam(defaultValue = "1") String code){
        logger.trace("ENDPOINT CALLED: /product");
        logger.trace("Input params: code = {}", code);

        var product = mysqlProductsRepository.findByProductCode(code);

        if(!product.isEmpty()) {
            return new ResponseEntity<>(product.get(0), headers, HttpStatus.OK.value());
        } else {
            return new ResponseEntity<>(new Product(), headers, HttpStatus.OK.value());
//            return new ResponseEntity<>(new ResponseEnvelope(
//                    String.format("Product with ID = %s does not exists", code), HttpStatus.NOT_MODIFIED.value()),
//                    headers, HttpStatus.NOT_MODIFIED.value());
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping(value = "/products/update",
            produces = { "application/json" }
    )
    public ResponseEntity<Object> updateProduct(@RequestParam(name = "product_id") long productId, @RequestParam(name = "product_code", required = false) String productCode, @RequestParam(name = "product_name", required = false) String productName,
                                                @RequestParam(name = "product_price", required = false) BigDecimal productPrice, @RequestParam(name = "product_origin", required = false) String productOrigin,
                                                @RequestParam(name = "product_category", required = false) String productCategory, @RequestParam(name = "product_brand", required = false) String productBrand,
                                                @RequestParam(name = "product_description", required = false) String productDescription){
        logger.trace("ENDPOINT CALLED: /products/update");
        logger.trace("Input params: productId = {}, productName = {}, productPrice = {}, productOrigin = {}", productId, productName, productPrice, productOrigin);
        var products = mysqlProductsRepository.findByProductId(productId);


        if(!products.isEmpty()){
            var rowsUpdated = mysqlProductsRepository.updateProduct(productId, productCode, productName, productPrice,
                    productOrigin, productCategory, productBrand, productDescription);
            var msg = String.format("Product with ID: %s updated successfully, number of rows updated %d", productId, rowsUpdated);
            headers.add(MESSAGE, msg);

            logger.trace("Product with ID = {} updated successfully with http status = {}, number of rows updated {}", productId, HttpStatus.OK.value(), rowsUpdated);

            return new ResponseEntity<>(new ResponseEnvelope(msg, HttpStatus.OK.value()), headers, HttpStatus.OK.value());
        } else {
            headers.add(MESSAGE, String.format("Product with ID = %s does not exists", productId));

            logger.trace("Product with ID = {} was not updated with http status = {}", productId, HttpStatus.NOT_MODIFIED.value());

            return new ResponseEntity<>(new ResponseEnvelope(
                    String.format("Product with ID = %s does not exists", productId), HttpStatus.NOT_MODIFIED.value()),
                    headers, HttpStatus.NOT_MODIFIED.value());
        }


    }

    @CrossOrigin(origins = "*")
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

    @CrossOrigin(origins = "*")
    @DeleteMapping("/remove")
    @Transactional
    public ResponseEntity<String> deleteProduct(@RequestParam(name = "product_id") long productId){
        var product = mysqlProductsRepository.findByProductId(productId);

        logger.trace("ENDPOINT CALLED: /remove");
        logger.trace("Input params: productId = {}", productId);

        if(!product.isEmpty()){
            mysqlProductsRepository.deleteProductByProductId(productId);

            return new ResponseEntity<>(String.format("Product with ID = %s deleted successfully!", productId), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(String.format("Product with ID = %s does not exists!", productId), HttpStatus.NOT_FOUND);
        }


    }
}
