package czu.bigdata.flightAnalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("czu.bigdata.flightAnalysis.mapper")
public class FlightAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightAnalysisApplication.class, args);
    }

}
