package czu.bigdata.flightAnalysis.service

import com.alibaba.fastjson.JSONArray
import czu.bigdata.flightAnalysis.utils.ObjGenerator
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class WordCountService extends Serializable{
  @Resource
  private val sparkSession: SparkSession = null

  def wordCount(filePath: String): JSONArray = {
    val filePath = "hdfs://node1:8020/test/input/wordcount.txt"
    val sc: SparkContext = sparkSession.sparkContext
    val fileRdd = sc.textFile(filePath).flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
    val wordCountArray: JSONArray = new JSONArray()
    fileRdd.collect().foreach(x => {
      wordCountArray.add(ObjGenerator.newJSON(Seq((x._1, x._2)): _*))
    })
    wordCountArray
  }
}