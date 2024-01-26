package com.mapper;

import com.model.Product;
import com.model.RawProduct;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "productName", target = "product_name")
    @Mapping(source = "productPrice", target = "product_price")
    @Mapping(source = "productOrigin", target = "product_origin")
    Product mapRawProductToProductDto(RawProduct rawProduct);

    @InheritInverseConfiguration
    RawProduct mapProductToRawProductDto(Product product);


}
