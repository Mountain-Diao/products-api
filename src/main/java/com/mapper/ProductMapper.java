package com.mapper;

import com.model.Product;
import com.model.RawProduct;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product mapRawProductToProductDto(RawProduct rawProduct);

    @InheritInverseConfiguration
    RawProduct mapProductToRawProductDto(Product product);


}
