package io.springbatch.springbatchlecture.batch.chunk.writer;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import io.springbatch.springbatchlecture.aTest.entity.ProductDetail;
import io.springbatch.springbatchlecture.aTest.repository.ProductDetailRepository;
import io.springbatch.springbatchlecture.aTest.repository.ProductRepository;
import io.springbatch.springbatchlecture.bTest.entity.TestEntity;
import io.springbatch.springbatchlecture.bTest.repository.BTestRepository;
import io.springbatch.springbatchlecture.domain.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CustomFileItemWriter implements ItemWriter<ProductVO> {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final BTestRepository bTestRepository;

    @Override
    public void write(Chunk<? extends ProductVO> chunk) throws Exception {
        List<Product> productList = new ArrayList<>();
        List<ProductDetail> productDetailList = new ArrayList<>();
        List<TestEntity> testEntityList = new ArrayList<>();

        chunk.getItems().forEach(getProductVO -> {
            Product product = getProductVO.toProduct();
            productList.add(product);

            ProductDetail productDetail = getProductVO.toProductDetail();
            productDetailList.add(productDetail);

            TestEntity test = getProductVO.toTestEntity();
            testEntityList.add(test);
        });

        productRepository.saveAll(productList);
        productDetailRepository.saveAll(productDetailList);
        //bTestRepository.saveAll(testEntityList);
    }

}
