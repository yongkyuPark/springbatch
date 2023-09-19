package io.springbatch.springbatchlecture.domain;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import io.springbatch.springbatchlecture.aTest.entity.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO implements Serializable {

    private Long id;
    private String name;
    private int price;
    private String type;
    private String requestDate;

    public Product toProduct() {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .type(type)
                .build();
    }

    public ProductDetail toProductDetail() {
        return ProductDetail.builder()
                .id(id)
                .name(name)
                .price(price)
                .type(type)
                .build();
    }

}
