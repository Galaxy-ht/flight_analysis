package czu.bigdata.flightAnalysis.utils;

import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

@Component
public class MySparkSession {

    private static SparkSession sparkSession;

    private static class SparkSessionHolder {
        private static final SparkSession INSTANCE = createSparkSession();
    }

    public static SparkSession getSparkSession() {
        return SparkSessionHolder.INSTANCE;
    }

    private static SparkSession createSparkSession() {
        // 在这里创建 SparkSession 的逻辑
        if (sparkSession == null) {
            sparkSession = SparkSession.builder()
                    .appName("test")
                    .master("spark://node1:7077")  // 适配你的 Spark 环境
                    .getOrCreate();
        }
        return sparkSession;
    }
}
