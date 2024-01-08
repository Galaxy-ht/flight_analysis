//package czu.bigdata.flightAnalysis.config
//
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs.FileSystem
//import org.apache.hadoop.fs.Path
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Component
//import javax.annotation.PostConstruct
//import javax.annotation.PreDestroy
//import java.io.IOException
//import java.net.URI
//import java.net.URISyntaxException
//
//
//@Component object HadoopConfig {
//  private val replication = "3"
//  private val blockSize = "2097152"
//}
//
//@Component class HadoopConfig {
//  private val hadoopUrl = "hdfs://node1:8020"
//  private val user = "root"
//  private var fs: FileSystem = null
//
//  @PostConstruct def init(): Unit = {
//    try {
//      // 获取连接集群的地址
//      val uri = new URI(hadoopUrl)
//      // 创建一个配置文件
//      val configuration = new Configuration
//      //设置配置文件中副本的数量
//      configuration.set("dfs.replication", HadoopConfig.replication)
//      //设置配置文件块大小
//      configuration.set("dfs.blocksize", HadoopConfig.blockSize)
//      //获取到了客户端对象
//      this.fs = FileSystem.get(uri, configuration, user)
//    } catch {
//      case e: Exception =>
//        throw new RuntimeException(e)
//    }
//  }
//
//  @PreDestroy def close(): Unit = {
//    // 关闭资源
//    try fs.close()
//    catch {
//      case e: IOException =>
//        throw new RuntimeException(e)
//    }
//  }
//
//  /**
//   * 将文件上传到hadoop目录
//   * */
//  def upToTmp(filePath: String, tarPath: String): Unit = {
//    try {
//      val srcPath = new Path(new URI(filePath))
//      val targetPath = new Path(new URI(tarPath))
//      // 参数解读：参数一：表示删除原数据  参数二：是否允许覆盖  参数三：元数据路径  参数四：目的地路径
//      fs.copyFromLocalFile(false, true, srcPath, targetPath)
//    } catch {
//      case e@(_: IOException | _: URISyntaxException) =>
//        throw new RuntimeException(e)
//    }
//  }
//
//  def getFs: FileSystem = fs
//}
