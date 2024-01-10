package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankVO {
    private String key;

    private String clickNumber;

    private String title;

    private String increases;
}