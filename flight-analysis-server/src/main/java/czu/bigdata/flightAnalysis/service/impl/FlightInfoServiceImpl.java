package czu.bigdata.flightAnalysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Function;
import czu.bigdata.flightAnalysis.entity.FlightInfo;
import czu.bigdata.flightAnalysis.enums.ArrivalType;
import czu.bigdata.flightAnalysis.enums.FlightType;
import czu.bigdata.flightAnalysis.exception.FastException;
import czu.bigdata.flightAnalysis.mapper.FlightInfoMapper;
import czu.bigdata.flightAnalysis.mybatis.MonthTableNameHandler;
import czu.bigdata.flightAnalysis.page.PageResult;
import czu.bigdata.flightAnalysis.query.FlightInfoQuery;
import czu.bigdata.flightAnalysis.query.Query;
import czu.bigdata.flightAnalysis.service.FlightInfoService;
import czu.bigdata.flightAnalysis.vo.CountVO;
import czu.bigdata.flightAnalysis.vo.RankVO;
import czu.bigdata.flightAnalysis.vo.XYChartVO;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
* @author MrHao
* @description 针对表【flight_all_info】的数据库操作Service实现
* @createDate 2024-01-09 20:07:56
*/
@Service
public class FlightInfoServiceImpl extends ServiceImpl<FlightInfoMapper, FlightInfo>
    implements FlightInfoService {


    @Override
    public PageResult<FlightInfo> page(FlightInfoQuery query) {
        IPage<FlightInfo> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), page.getRecords());
    }

    @Override
    public CountVO getCount() {
        Date currentTime = new Date();
        Date yesterday = getYesterday(currentTime);

        MonthTableNameHandler.setData(currentTime);
        CountVO countVO = new CountVO();

        List<FlightInfo> todayFlight = this.getTodayFlight();
        List<FlightInfo> yesterdayFlight = this.getDayFlight(yesterday);

        countVO.setTodayCount(todayFlight.size());
        countVO.setFlyingCount(getFlight(todayFlight, FlightType.FLYING).size());
        countVO.setLateCount(getArrivalFlight(todayFlight, ArrivalType.LATE).size());

        DecimalFormat df = new DecimalFormat("#.0");
        countVO.setIncrease(Double.valueOf(df.format(((todayFlight.size() - yesterdayFlight.size()) / (double) yesterdayFlight.size()) * 100)));

        return countVO;
    }

    @Override
    public List<XYChartVO> getDayChart(Integer days) {
        if (days < 7) {
            days = 7;
        }
        Date currentTime = new Date();
        MonthTableNameHandler.setData(currentTime);

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(days);

        QueryWrapper<FlightInfo> wrapper = new QueryWrapper<>();
        wrapper.ge("planned_departure_time", startDate.atStartOfDay());
        wrapper.le("planned_departure_time", currentDate.plusDays(1).atStartOfDay());
        wrapper.groupBy("DATE(planned_departure_time)");
        wrapper.select("DATE(planned_departure_time) as x, count(1) as y");


        return this.baseMapper.selectMaps(wrapper).stream().map(item -> new XYChartVO((Date) item.get("x"), (Long) item.get("y"))).collect(Collectors.toList());
    }

    @Override
    public void updateActualTime() {
        Date currentTime = new Date();
        MonthTableNameHandler.setData(currentTime);

        double probability = new Random().nextDouble();
        UpdateWrapper<FlightInfo> wrapper = new UpdateWrapper<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedCurrentTime = formatter.format(currentTime);
        wrapper.apply("DATE_FORMAT(planned_departure_time, '%Y-%m-%d %H:%i') = {0}", formattedCurrentTime);
        if (probability < 0.9) {
            wrapper.set("actual_departure_time", getRandomTime(currentTime, 31, 0));
        } else {
            wrapper.set("is_cancel", 1);
        }
        this.update(wrapper);

        double finalProbability = new Random().nextDouble();
        wrapper.clear();
        wrapper.eq("planned_arrival_time", currentTime);
        wrapper.eq("is_cancel", 0);
        this.list(wrapper).forEach(item -> {
            if (finalProbability < 0.3) {
                item.setActualArrivalTime(getRandomTime(currentTime, 61, 60));
                item.setIsLate(1);
            } else {
                item.setActualArrivalTime(getRandomTime(currentTime, 61, 0));
            }
            this.updateById(item);
        });
    }

    @Override
    public List<RankVO> getRanking(String type) {
        Function<FlightInfo, String> functionType = null;
        if (Objects.equals(type, "text")) {
            functionType = FlightInfo::getArrivalProvince;
        } else if (Objects.equals(type, "image")) {
            functionType = FlightInfo::getArrivalCity;
        } else if (Objects.equals(type, "video")) {
            functionType = FlightInfo::getFlightCode;
        } else {
            throw new FastException("Error!");
        }
        Date yesterday = getYesterday(new Date());


        List<FlightInfo> todayFlight = getTodayFlight();
        List<FlightInfo> yesterdayFlight = getDayFlight(yesterday);

        // 根据 arrival_province 进行分组求和
        Map<String, Long> todayProvinceSumMap = todayFlight.stream()
                .collect(Collectors.groupingBy(functionType, Collectors.counting()));

        // 根据 arrival_province 进行分组求和
        Map<String, Long> yesterdayProvinceSumMap = yesterdayFlight.stream()
                .collect(Collectors.groupingBy(functionType, Collectors.counting()));

        AtomicInteger i = new AtomicInteger(1);
        // 计算增长率
        List<RankVO> result = todayProvinceSumMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    long todayCount = entry.getValue();
                    long yesterdayCount = yesterdayProvinceSumMap.getOrDefault(entry.getKey(), 0L);
                    double increases = yesterdayCount == 0 ? 0.0 : ((double) todayCount - yesterdayCount) / yesterdayCount * 100;
                    return new RankVO(String.valueOf(i.getAndIncrement()), String.valueOf(todayCount), entry.getKey(), String.format("%.2f", increases));
                })
                .collect(Collectors.toList());
        return result;
    }

    private IPage<FlightInfo> getPage(Query query) {
        if (query.getPageNo() == null && query.getPageSize() == null) {
            query.setPageNo(1);
            query.setPageSize(10);
        }
        return new Page<>(query.getPageNo(), query.getPageSize());
    }

    private QueryWrapper<FlightInfo> getWrapper(FlightInfoQuery query) {
        QueryWrapper<FlightInfo> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    private List<FlightInfo> getDayFlight(Date date) {
        MonthTableNameHandler.setData(date);

        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        QueryWrapper<FlightInfo> wrapper = new QueryWrapper<>();
        wrapper.between("planned_departure_time",localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());

        return this.list(wrapper);
    };


    private List<FlightInfo> getTodayFlight() {
        Date currentTime = new Date();
        MonthTableNameHandler.setData(currentTime);

        QueryWrapper<FlightInfo> wrapper = new QueryWrapper<>();
        wrapper.between("planned_departure_time", LocalDate.now().atStartOfDay(), LocalDateTime.now());

        return this.list(wrapper);
    }

    private List<FlightInfo> getArrivalFlight(List<FlightInfo> flightList, ArrivalType type){
        flightList = this.getFlight(flightList, FlightType.ARRIVED);
        switch (type) {
            case LATE:
                return flightList.stream()
                        .filter(flight -> flight.getIsLate().equals(1))
                        .collect(Collectors.toList());
            case BEFORE:
                return flightList.stream()
                        .filter(flight -> flight.getPlannedArrivalTime().equals(flight.getActualArrivalTime()))
                        .collect(Collectors.toList());
            case ON_TIME:
                return flightList.stream()
                        .filter(flight -> flight.getPlannedArrivalTime().before(flight.getActualArrivalTime()))
                        .collect(Collectors.toList());
            default:
                return flightList;
        }
    }

    private List<FlightInfo> getFlight(List<FlightInfo> flightList, FlightType type) {
        switch (type) {
            case CANCEL:
                return flightList.stream()
                        .filter(flight -> flight.getIsCancel().equals(1))
                        .collect(Collectors.toList());
            case UNFLY:
                return flightList.stream()
                        .filter(flight -> flight.getIsCancel().equals(0) && flight.getActualDepartureTime() == null)
                        .collect(Collectors.toList());
            case FLYING:
                return flightList.stream()
                        .filter(flight -> flight.getActualDepartureTime() != null && flight.getActualArrivalTime() == null)
                        .collect(Collectors.toList());
            case ARRIVED:
                return flightList.stream()
                        .filter(flight -> flight.getActualArrivalTime() != null)
                        .collect(Collectors.toList());
            default:
                return flightList;
        }
    }

    private Date getRandomTime(Date currentDate, Integer minutes, Integer base) {
        int randomMinutes = new Random().nextInt(minutes) + base;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        boolean isAdd = new Random().nextBoolean();

        if (isAdd) {
            calendar.add(Calendar.MINUTE, randomMinutes);
        } else {
            calendar.add(Calendar.MINUTE, -randomMinutes);
        }

        return calendar.getTime();
    }

    private Date getYesterday(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }
}





