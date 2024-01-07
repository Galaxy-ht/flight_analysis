package czu.bigdata.flightAnalysis.controller;

import czu.bigdata.flightAnalysis.AnalysisADR;
import czu.bigdata.flightAnalysis.config.HadoopConfig;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import czu.bigdata.flightAnalysis.service.TestService;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @Resource
    private SparkSession sparkSession;

    @Resource
    private HadoopConfig hadoopConfig;

    @Resource
    private TestService testService;

    @RequestMapping("/")
    public void test(@RequestParam("sql") @NotBlank String sql) {
        AnalysisADR.analysis(sparkSession);
    }

//    @RequestMapping("/")
//    public String test1() throws IOException {
//        FileSystem fs = hadoopConfig.getFs();
//        FSDataInputStream open = fs.open(new Path("/test/input/wordcount.txt"));
//        BufferedReader reader = new BufferedReader(new InputStreamReader(open));
//        // 读取文件内容并创建RDD
//        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
//        JavaRDD<String> lines = sparkContext.parallelize(reader.lines().collect(Collectors.toList()));
//
//        // 执行词频统计操作
//        Map<String, Long> wordCountMap = lines
//                .flatMap(line -> Arrays.asList(line.split("\\s+")).iterator())
//                .countByValue();
//
//        // 关闭资源
//        reader.close();
//        sparkContext.close();
//
//        // 返回词频统计结果
//        return wordCountMap.toString();
//    }

    @RequestMapping("/test")
    public String test2() {
        return sparkSession.sessionUUID();
    }
}
