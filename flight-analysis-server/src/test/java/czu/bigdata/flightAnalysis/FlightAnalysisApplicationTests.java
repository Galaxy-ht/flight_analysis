package czu.bigdata.flightAnalysis;


import czu.bigdata.flightAnalysis.config.HadoopConfig;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Date;

@SpringBootTest
class FlightAnalysisApplicationTests {

    @Resource
    private HadoopConfig hadoopConfig;

    @Resource
    private SparkSession sparkSession;

    @Test
    void contextLoads() throws URISyntaxException, IOException {
        FileSystem fs = hadoopConfig.getFs();
        FSDataInputStream open = fs.open(new Path("/test/input/wordcount.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(open));
        String lineTxt = "";
        StringBuffer sb = new StringBuffer();
        while ((lineTxt = reader.readLine()) != null) {
            sb.append(lineTxt);
        }
        System.out.println(sb);
        open.close();
        fs.close();
    }

    @Test
    void sparkTest(){
        System.out.println(sparkSession.sessionUUID());
    }

    @Test
    void test() {
        Date date = new Date();
        System.out.println(date.getYear() + 1900);
        System.out.println(String.format("%0"+2+"d", date.getMonth() + 1));
    }
}
