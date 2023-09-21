package io.springbatch.springbatchlecture.batch.classifier;

import io.springbatch.springbatchlecture.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> {

    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

    @Override
    public ItemProcessor<?, ? extends ApiRequestVO> classify(ProductVO classifiable) {
        return processorMap.get(classifiable.getType());
    }

    public void setProcessorMap(Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap) {
        this.processorMap = processorMap;
    }
}
