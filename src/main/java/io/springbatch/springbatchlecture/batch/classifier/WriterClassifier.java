package io.springbatch.springbatchlecture.batch.classifier;

import io.springbatch.springbatchlecture.domain.ApiRequestVO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class WriterClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();

    @Override
    public T classify(C classifiable) {
        return (T)writerMap.get(((ApiRequestVO)classifiable).getProductVO().getType());
    }

    public void setProcessorMap(Map<String, ItemWriter<ApiRequestVO>> writerMap) {
        this.writerMap = writerMap;
    }
}
