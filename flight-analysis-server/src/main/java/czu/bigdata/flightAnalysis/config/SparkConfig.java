//package czu.bigdata.flightAnalysis.config;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.sql.SparkSession;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Configuration
//public class SparkConfig {
//
//    @Value("${spark.path}")
//    private String sparkPath;
//
//    @Bean
//    public SparkConf sparkConf() {
//        return new SparkConf()
//                .setAppName("flightAnalysis")
//                .setMaster(sparkPath);
//    }
//
//    @Bean public JavaSparkContext javaSparkContext() {
//        return new JavaSparkContext(sparkConf());
//    }
//
//    @Bean
//    public SparkSession sparkSession() {
//        return SparkSession.builder().sparkContext(javaSparkContext().sc()).getOrCreate();
//    }
//
//}
