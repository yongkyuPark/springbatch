package io.springbatch.springbatchlecture.batch.chunk.processor;

import io.springbatch.springbatchlecture.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor1 implements ItemProcessor<ProductVO, ApiRequestVO> {
    @Override
    public ApiRequestVO process(ProductVO item) throws Exception {
        return ApiRequestVO.builder()
                .id(item.getId())
                .productVO(item)
                .build();
    }
}
