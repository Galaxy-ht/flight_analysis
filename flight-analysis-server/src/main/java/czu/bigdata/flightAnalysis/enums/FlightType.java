package czu.bigdata.flightAnalysis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlightType {
    CANCEL(0, "取消"),
    UNFLY(1, "未起飞"),
    FLYING(2, "飞行中"),
    ARRIVED(3, "已到达");

    private final int code;
    private final String msg;
}
