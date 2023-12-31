package io.springbatch.springbatchlecture.aTest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Table(name = "productDtl")
@Builder
public class ProductDetail {

    @Id
    private Long id;
    private String name;
    private int price;
    private String type;

}
