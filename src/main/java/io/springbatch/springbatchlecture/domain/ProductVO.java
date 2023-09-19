package io.springbatch.springbatchlecture.domain;

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

}
