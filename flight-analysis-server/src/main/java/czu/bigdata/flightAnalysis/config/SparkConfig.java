package czu.bigdata.flightAnalysis.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class SparkConfig {

    @Value("${spark.path}")
    private String sparkPath;

    @Bean
    public SparkConf sparkConf() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String currentIp = inetAddress.getHostAddress();
        SparkConf conf = new SparkConf()
                .setAppName("flightAnalysis")
                .setMaster(sparkPath);
        conf.set("spark.driver.host", currentIp).set("spark.driver.port", "9999");
        return conf;
    }

    @Bean
    public SparkSession sparkSession() throws UnknownHostException {
        return SparkSession.builder().sparkContext(javaSparkContext().sc()).getOrCreate();
    }

    @Bean public JavaSparkContext javaSparkContext() throws UnknownHostException {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

}
