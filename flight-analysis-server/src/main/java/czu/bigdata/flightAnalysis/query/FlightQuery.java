package czu.bigdata.flightAnalysis.query;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FlightQuery extends Query{
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
}
