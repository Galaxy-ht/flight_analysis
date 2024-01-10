package czu.bigdata.flightAnalysis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArrivalType {
    LATE(0, "晚点"),
    ON_TIME(1, "准点"),
    BEFORE(2, "提前");

    private final int code;
    private final String msg;
}
