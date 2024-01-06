package czu.bigdata.flightAnalysis;


import java.io.BufferedReader;
import czu.bigdata.flightAnalysis.config.HadoopConfig;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

@SpringBootTest
class FlightAnalysisApplicationTests {

    @Resource
    private HadoopConfig hadoopConfig;
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

}
