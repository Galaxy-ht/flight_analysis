package czu.bigdata.flightAnalysis.controller;

import czu.bigdata.flightAnalysis.service.FlightInfoService;
import czu.bigdata.flightAnalysis.utils.Result;
import czu.bigdata.flightAnalysis.vo.CountVO;
import czu.bigdata.flightAnalysis.vo.RankVO;
import czu.bigdata.flightAnalysis.vo.XYChartVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {

    @Resource
    private FlightInfoService service;

    @GetMapping("/getCount")
    public Result<CountVO> getCount() {
        return Result.ok(service.getCount());
    }

    @GetMapping("/getDayChart")
    public Result<List<XYChartVO>> getDayChart(@RequestParam Integer days) {
        return Result.ok(service.getDayChart(days));
    }

    @GetMapping("/getRanking")
    public Result<List<RankVO>> getRanking(@RequestParam String type) {
        return Result.ok(service.getRanking(type));
    }

}
