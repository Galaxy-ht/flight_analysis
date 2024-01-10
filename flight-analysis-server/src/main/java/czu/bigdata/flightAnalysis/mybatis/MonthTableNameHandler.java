package czu.bigdata.flightAnalysis.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import lombok.Getter;

import java.util.Date;

@Getter
public class MonthTableNameHandler implements TableNameHandler {

    //用于记录哪些表可以使用该月份动态表名处理器（即哪些表按月分表）
    private final String tableNames = "flight_all_info";

    //每个请求线程维护一个month数据，避免多线程数据冲突。所以使用ThreadLocal
    private static final ThreadLocal<Date> MONTH_DATA = new ThreadLocal<>();

    //设置请求线程的month数据
    public static void setData(Date date) {
        MONTH_DATA.set(date);
    }

    //删除当前请求线程的month数据
    public static void removeData() {
        MONTH_DATA.remove();
    }

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (this.tableNames.contains(tableName)){
            Date date = MONTH_DATA.get();
            String year = String.valueOf(date.getYear() + 1900);
            String month = String.format("%0"+2+"d", date.getMonth() + 1);
            return tableName + "_" + year + "_" + month;  //表名增加月份后缀
        }else{
            return tableName;   //表名原样返回
        }
    }

}