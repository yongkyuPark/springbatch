package io.springbatch.springbatchlecture.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVO {

    private long id;
    private ProductVO productVO;
    private ApiResponseVO apiResponseVO;


}
