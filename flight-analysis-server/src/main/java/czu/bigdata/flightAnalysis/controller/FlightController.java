package czu.bigdata.flightAnalysis.controller;

import czu.bigdata.flightAnalysis.vo.*;
import czu.bigdata.flightAnalysis.service.FlightInfoService;
import czu.bigdata.flightAnalysis.utils.Result;
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

    @GetMapping("/getPopularFlight")
    public Result<List<RankingVO>> getPopularFlight() {
        return Result.ok(service.getPopularFlight());
    }

    @GetMapping("/content-publish")
    public Result<List<BarChartVO>> getBarChart() {
        return Result.ok(service.getBarChart());
    }

    @PostMapping("/content-period-analysis")
    public Result<LineChartVO> getLineChart() {
        return Result.ok(service.getLineChart());
    }
}
