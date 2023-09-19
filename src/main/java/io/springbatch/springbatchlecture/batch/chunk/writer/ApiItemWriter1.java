package io.springbatch.springbatchlecture.batch.chunk.writer;

import io.springbatch.springbatchlecture.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.domain.ApiResponseVO;
import io.springbatch.springbatchlecture.service.AbstractApiService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

public class ApiItemWriter1 extends FlatFileItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    public ApiItemWriter1(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(Chunk<? extends ApiRequestVO> chunk) throws Exception {
        ApiResponseVO responseVO = apiService.service(chunk.getItems());
        System.out.println("responseVO = " + responseVO);

        chunk.getItems().forEach(item -> item.setApiResponseVO(responseVO));

        super.setResource(new FileSystemResource("/Users/yongkyu/springbatchstudy/springbatchlecture/src/main/resources/product1.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>()); // 구분자 방식
        super.setAppendAllowed(true); // 추가
        super.write(chunk);
    }
}
