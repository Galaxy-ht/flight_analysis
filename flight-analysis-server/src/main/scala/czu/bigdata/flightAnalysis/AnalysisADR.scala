package czu.bigdata.flightAnalysis

import org.apache.spark.sql.SparkSession

object AnalysisADR {
  def analysis(sparkSession:SparkSession) = {
    val spark = sparkSession

    val url = "jdbc:mysql://localhost:3306/yl"
    val drugTable = "drug"
    val reactionTable = "reaction"
    val prop = new java.util.Properties()
    prop.put("driver", "com.mysql.jdbc.Driver")
    prop.put("user", "root")
    prop.put("password", "root")

    val drug = spark.read.jdbc(url, drugTable, prop).show()
  }
}
