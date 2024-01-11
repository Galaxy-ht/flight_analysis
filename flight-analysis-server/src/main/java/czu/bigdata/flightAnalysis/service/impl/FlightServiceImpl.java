package czu.bigdata.flightAnalysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import czu.bigdata.flightAnalysis.entity.Flight;
import czu.bigdata.flightAnalysis.page.PageResult;
import czu.bigdata.flightAnalysis.query.FlightQuery;
import czu.bigdata.flightAnalysis.query.Query;
import czu.bigdata.flightAnalysis.service.FlightService;
import czu.bigdata.flightAnalysis.mapper.FlightMapper;
import org.springframework.stereotype.Service;

/**
* @author MrHao
* @description 针对表【flight】的数据库操作Service实现
* @createDate 2024-01-11 13:05:06
*/
@Service
public class FlightServiceImpl extends ServiceImpl<FlightMapper, Flight>
    implements FlightService{

    @Override
    public PageResult<Flight> page(FlightQuery query) {
        IPage<Flight> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), page.getRecords());

    }

    private IPage<Flight> getPage(Query query) {
        if (query.getPageNo() == null && query.getPageSize() == null) {
            query.setPageNo(1);
            query.setPageSize(10);
        }
        return new Page<>(query.getPageNo(), query.getPageSize());
    }

    private QueryWrapper<Flight> getWrapper(FlightQuery query) {
        QueryWrapper<Flight> wrapper = new QueryWrapper<>();

        return wrapper;
    }
}




