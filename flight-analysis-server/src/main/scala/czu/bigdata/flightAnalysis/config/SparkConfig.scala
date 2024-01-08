package czu.bigdata.flightAnalysis.config

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.springframework.context.annotation.{Bean, Configuration}

import java.net.UnknownHostException



@Configuration class SparkConfig {
  private val sparkPath = "spark://node1:7077"
  System.setProperty("HADOOP_USER_NAME","root")
  private val executorCores = 8
  private val coresMax = executorCores * 8//个数取决于目前有多少资源（cores）可用，不要一下把资源占用满
  private val partitionNum = coresMax * 3//spark.cores.max * 3
  val conf: SparkConf = new SparkConf()
    .setAppName("flightAnalysis")
    .setMaster(sparkPath)//Spark集群地址
    .setIfMissing("spark.driver.host", "192.168.88.1")//必须添加本地IP（运行Idea的电脑)
    .setJars(List("D:\\Users\\MrHao\\IdeaProjects\\flight-analysis\\flight-analysis-server\\target\\flight-analysis-0.0.1-SNAPSHOT.jar"))
  @Bean
  @throws[UnknownHostException]
  def sparkSession: SparkSession = SparkSession.builder.config(conf).getOrCreate
}
