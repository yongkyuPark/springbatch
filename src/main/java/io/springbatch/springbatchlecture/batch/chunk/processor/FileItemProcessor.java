package io.springbatch.springbatchlecture.batch.chunk.processor;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import io.springbatch.springbatchlecture.domain.ProductVO;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {
    @Override
    public Product process(ProductVO item) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(item, Product.class);

        return product;
    }
}
