package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BarChartVO {

    private String name;

    private List<String> x;

    public List<Integer> y;

}
