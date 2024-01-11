package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XYChartVO {

    private String name;

    private String x;

    private String y;

    public XYChartVO(String x, String y) {
        this.x = x;
        this.y = y;
    }
}
