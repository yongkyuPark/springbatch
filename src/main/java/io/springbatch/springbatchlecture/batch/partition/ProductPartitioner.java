package io.springbatch.springbatchlecture.batch.partition;

import io.springbatch.springbatchlecture.annotation.MysqlRepository;
import io.springbatch.springbatchlecture.domain.ProductVO;
import io.springbatch.springbatchlecture.batch.job.api.QueryGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@MysqlRepository
public class ProductPartitioner implements Partitioner {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        // database 에서 type을 group by 해서 가져온다. type 기준으로 파티셔닝 하기 위해서
        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;

        for(int i=0; i<productList.length; i++) {
            ExecutionContext value = new ExecutionContext();

            result.put("partition" + number, value);
            value.put("product", productList[i]); // ExecutionContext에 type 정보 저장

            number++;
        }
        return result;
    }
}
