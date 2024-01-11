package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingVO {
    private Integer ranking;

    private String flightCode;

    private String departure;

    private String arrival;
}
