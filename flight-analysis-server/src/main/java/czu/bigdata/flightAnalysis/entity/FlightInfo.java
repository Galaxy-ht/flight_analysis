package czu.bigdata.flightAnalysis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description flight_info
 * @author Tao
 * @date 2024-01-09
 */
@TableName("flight_all_info")
@Data
public class FlightInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * departure_airport
     */
    private String departureAirport;

    /**
     * departure_province
     */
    private String departureProvince;

    /**
     * departure_city
     */
    private String departureCity;

    /**
     * departure_longitude
     */
    private Double departureLongitude;

    /**
     * departure_latitude
     */
    private Double departureLatitude;

    /**
     * arrival_airport
     */
    private String arrivalAirport;

    /**
     * arrival_province
     */
    private String arrivalProvince;

    /**
     * arrival_city
     */
    private String arrivalCity;

    /**
     * arrival_longitude
     */
    private Double arrivalLongitude;

    /**
     * arrival_latitude
     */
    private Double arrivalLatitude;

    /**
     * flight_number
     */
    private String flightCode;

    /**
     * flight_airlines
     */
    private String flightCompany;

    /**
     * weather
     */
    private String weather;

    /**
     * temperature
     */
    private Double temperature;

    /**
     * departure_airport_has_case
     */
    private Integer departureAirportHasCase;

    /**
     * arrival_airport_has_case
     */
    private Integer arrivalAirportHasCase;

    /**
     * aircraft_number
     */
    private Integer aircraftCode;

    /**
     * is_cancel
     */
    private Integer isCancel;

    /**
     * late_time
     */
    private Integer lateTime;

    /**
     * is_late
     */
    private Integer isLate;

    /**
     * planned_departure_time
     */
    private Date plannedDepartureTime;

    /**
     * planned_arrival_time
     */
    private Date plannedArrivalTime;

    /**
     * actual_departure_time
     */
    private Date actualDepartureTime;

    /**
     * actual_arrival_time
     */
    private Date actualArrivalTime;


}