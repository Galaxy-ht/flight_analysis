package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LineChartVO {

    private List<String> xAxis;

    private List<DataVO> data;

}
