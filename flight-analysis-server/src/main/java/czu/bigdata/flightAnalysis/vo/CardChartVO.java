package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CardChartVO {
    private Integer count;

    private String growth;

    List<XYChartVO> chartData;
}
