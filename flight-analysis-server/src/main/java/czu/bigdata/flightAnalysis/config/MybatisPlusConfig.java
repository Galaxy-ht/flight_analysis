package czu.bigdata.flightAnalysis.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import czu.bigdata.flightAnalysis.mybatis.MonthTableNameHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        Map<String, TableNameHandler> tableNameHandlerMap = new HashMap<>();
        MonthTableNameHandler flight_all_info = new MonthTableNameHandler();
        tableNameHandlerMap.put(flight_all_info.getTableNames(), flight_all_info);

        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(tableNameHandlerMap);

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //以拦截器的方式处理表名称
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);

        return interceptor;
    }


}
