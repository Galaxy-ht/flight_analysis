package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PieChartModel {
    private String total;

    private String late;

    private String onTime;

    private String before;
}
