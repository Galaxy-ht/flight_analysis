package czu.bigdata.flightAnalysis.vo;

import lombok.Data;

@Data
public class CountVO {
    private Integer todayCount;

    private Integer flyingCount;

    private Integer lateCount;

    private Double increase;
}
