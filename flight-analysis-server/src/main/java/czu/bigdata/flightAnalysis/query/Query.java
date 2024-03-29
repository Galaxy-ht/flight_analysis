package czu.bigdata.flightAnalysis.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * 查询公共参数
 *
 * @author 阿沐 babamu@126.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query {
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private int countTotal;

    private int pageTotal;

    private Integer start;

    private Integer end;

    String orderBy;

}
