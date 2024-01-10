package czu.bigdata.flightAnalysis.service

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.springframework.stereotype.Service

@Service
class StreamingWordCountService extends Serializable {
  def wordCount(): Int = {
    val conf: SparkConf = new SparkConf()
      .setAppName("flightAnalysis")
      .setMaster("spark://node1:7077")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .setIfMissing("spark.driver.host", "192.168.88.1")//必须添加本地IP（运行Idea的电脑)
      .setJars(List("D:\\Users\\MrHao\\IdeaProjects\\flight-analysis\\flight-analysis-server\\target\\flight-analysis-0.0.1-SNAPSHOT.jar"))

    val ssc: StreamingContext = new StreamingContext(conf, Seconds(1))
    val lines = ssc.socketTextStream("192.168.88.1", 9999)
    lines.print()
    ssc.start()
    ssc.awaitTermination()
    1
  }
}
