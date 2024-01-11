package czu.bigdata.flightAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName flight
 */
@TableName(value ="flight")
@Data
public class Flight implements Serializable {
    @TableId
    private Integer id;

    private String flightCode;

    private String departureProvince;

    private String departureCity;

    private String departureAirport;

    private BigDecimal departureLongitude;

    private BigDecimal departureLatitude;

    private String arrivalProvince;

    private String arrivalCity;

    private String arrivalAirport;

    private BigDecimal arrivalLongitude;

    private BigDecimal arrivalLatitude;

    private Date plannedDepartureTime;

    private Date plannedArrivalTime;

    private static final long serialVersionUID = 1L;
}