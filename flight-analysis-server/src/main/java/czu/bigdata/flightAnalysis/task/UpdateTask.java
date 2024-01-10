//package czu.bigdata.flightAnalysis.task;
//
//import czu.bigdata.flightAnalysis.service.FlightInfoService;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Component
//@EnableScheduling
//public class UpdateTask {
//    @Resource
//    private FlightInfoService service;
//
//    @Scheduled(fixedRate = 30000)
//    public void update() {
//        service.updateActualTime();
//    }
//}
