package czu.bigdata.flightAnalysis.service

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
 
@Service
class TestService extends LazyLogging{
  @Autowired
  private val sparkSession: SparkSession = null
  def getData(sql: String): Unit = {
    logger.info(sql)
    val df = sparkSession.sql(sql)
    df.show()
    logger.info(df.count().toString)
  }
}