package czu.bigdata.flightAnalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import czu.bigdata.flightAnalysis.entity.FlightInfo;
import czu.bigdata.flightAnalysis.page.PageResult;
import czu.bigdata.flightAnalysis.query.FlightInfoQuery;
import czu.bigdata.flightAnalysis.vo.CountVO;
import czu.bigdata.flightAnalysis.vo.RankVO;
import czu.bigdata.flightAnalysis.vo.XYChartVO;

import java.util.List;

/**
* @author MrHao
* @description 针对表【flight_all_info】的数据库操作Service
* @createDate 2024-01-09 20:07:56
*/
public interface FlightInfoService extends IService<FlightInfo> {
    PageResult<FlightInfo> page(FlightInfoQuery query);

    CountVO getCount();

    List<XYChartVO> getDayChart(Integer days);

    void updateActualTime();

    List<RankVO> getRanking(String type);
}
