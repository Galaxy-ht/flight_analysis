package czu.bigdata.flightAnalysis.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class HadoopConfig {
    @Value("${hdfs.path}")
    private String hadoopUrl;

    @Value("${hdfs.username}")
    private String user;

    private static final String replication = "3";

    private static final String blockSize = "2097152";
    private FileSystem fs;


    @PostConstruct
    public void init() {
        try {
            // 获取连接集群的地址
            URI uri = new URI(hadoopUrl);
            // 创建一个配置文件
            Configuration configuration = new Configuration();
            //设置配置文件中副本的数量
            configuration.set("dfs.replication", replication);
            //设置配置文件块大小
            configuration.set("dfs.blocksize", blockSize);
            //获取到了客户端对象
            this.fs = FileSystem.get(uri, configuration, user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void close() {
        // 关闭资源
        try {
            fs.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将文件上传到hadoop目录
     **/
    public void upToTmp(String filePath, String tarPath) {
        try {
            Path srcPath = new Path(new URI(filePath));
            Path targetPath = new Path(new URI(tarPath));
            // 参数解读：参数一：表示删除原数据  参数二：是否允许覆盖  参数三：元数据路径  参数四：目的地路径
            fs.copyFromLocalFile(false, true, srcPath, targetPath);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public FileSystem getFs() {
        return fs;
    }
}
