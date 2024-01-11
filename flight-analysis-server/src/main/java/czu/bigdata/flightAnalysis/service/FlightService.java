package czu.bigdata.flightAnalysis.service;

import czu.bigdata.flightAnalysis.entity.Flight;
import com.baomidou.mybatisplus.extension.service.IService;
import czu.bigdata.flightAnalysis.entity.FlightInfo;
import czu.bigdata.flightAnalysis.page.PageResult;
import czu.bigdata.flightAnalysis.query.FlightInfoQuery;
import czu.bigdata.flightAnalysis.query.FlightQuery;

/**
* @author MrHao
* @description 针对表【flight】的数据库操作Service
* @createDate 2024-01-11 13:05:06
*/
public interface FlightService extends IService<Flight> {
    PageResult<Flight> page(FlightQuery query);
}
