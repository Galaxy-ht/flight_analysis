package czu.bigdata.flightAnalysis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataVO {

    private String name;

    private List<Integer> value;

}
