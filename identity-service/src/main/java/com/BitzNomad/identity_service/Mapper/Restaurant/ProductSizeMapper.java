package com.BitzNomad.identity_service.Mapper.Restaurant;

import com.BitzNomad.identity_service.DtoReponese.ProductSizeReponeseDTO;
import com.BitzNomad.identity_service.DtoRequest.ProductSizeRequestDTO;
import com.BitzNomad.identity_service.Entity.Restaurant.Product;
import com.BitzNomad.identity_service.Entity.Restaurant.ProductSize;
import com.BitzNomad.identity_service.Respository.RestaurantRepository.ProductRepository;
import com.BitzNomad.identity_service.Respository.RestaurantRepository.SizeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductSizeMapper {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SizeRepository sizeRepository;

    public ProductSize convertProductSizeRequsetToProduct(ProductSizeRequestDTO productSizeRequestDTO) {
        ProductSize productSize = modelMapper.map(productSizeRequestDTO, ProductSize.class);
        productSize.setProduct(
                productRepository.findById(productSizeRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product Not find!"))
        );
        productSize.setSize(
                sizeRepository.findById(productSizeRequestDTO.getSizeid()).orElseThrow(() -> new RuntimeException("Size Not find!"))
        );
        return productSize;
    }

    public ProductSizeReponeseDTO convertProductSizeToProductSizeReponeseDTO(ProductSize productSize) {
        ProductSizeReponeseDTO productSizeReponeseDTO = modelMapper.map(productSize, ProductSizeReponeseDTO.class);
        productSizeReponeseDTO.setProductName(productSize.getProduct().getName());
        productSizeReponeseDTO.setSizeName(productSize.getSize().getName());
        return productSizeReponeseDTO;
    }
}
