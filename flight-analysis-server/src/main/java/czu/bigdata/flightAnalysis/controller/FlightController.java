package czu.bigdata.flightAnalysis.controller;

import czu.bigdata.flightAnalysis.vo.CardChartVO;
import czu.bigdata.flightAnalysis.service.FlightInfoService;
import czu.bigdata.flightAnalysis.utils.Result;
import czu.bigdata.flightAnalysis.vo.CountVO;
import czu.bigdata.flightAnalysis.vo.PieChartModel;
import czu.bigdata.flightAnalysis.vo.RankVO;
import czu.bigdata.flightAnalysis.vo.XYChartVO;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getPieChart")
    public Result<PieChartModel> getPieChart() {
        return Result.ok(service.getPieChart());
    };

    @PostMapping("/public-opinion-analysis")
    public Result<CardChartVO> getRealTimeChart(@RequestBody String quota) {
        return Result.ok(service.getRealTimeChart(quota));
    }
}
