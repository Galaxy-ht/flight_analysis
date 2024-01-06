package czu.bigdata.flightAnalysis.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class SparkConfig {

    @Bean
    public SparkConf sparkConf(){
        SparkConf conf = new SparkConf()
                .setAppName("flightAnalysis")
                .setMaster("local[*]");
        conf.set("spark.driver.host", "100.78.92.56").set("spark.driver.port", "9999");
        return conf;
    }

    @Bean
    public SparkSession sparkSession(){
        return SparkSession.builder().sparkContext(javaSparkContext().sc()).getOrCreate();
    }

    @Bean public JavaSparkContext javaSparkContext(){
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

}
