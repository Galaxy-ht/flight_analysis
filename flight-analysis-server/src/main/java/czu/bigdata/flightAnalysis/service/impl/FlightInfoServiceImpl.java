package czu.bigdata.flightAnalysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Function;
import czu.bigdata.flightAnalysis.vo.*;
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
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


        return this.baseMapper.selectMaps(wrapper).stream().map(item -> new XYChartVO(item.get("x").toString(), String.valueOf(item.get("y")))).collect(Collectors.toList());
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
                Date randomTime = getRandomTime(currentTime, 61, 120);
                item.setActualArrivalTime(randomTime);
                if (randomTime.after(currentTime)) {
                    item.setIsLate(1);
                    item.setLateTime((int) Math.ceil(randomTime.getTime() - currentTime.getTime()) / (60 * 60 * 1000));
                }
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

    @Override
    public PieChartModel getPieChart() {
        List<FlightInfo> todayFlight = getTodayFlight();
        List<FlightInfo> total = getFlight(todayFlight, FlightType.ARRIVED);
        List<FlightInfo> late = getArrivalFlight(total, ArrivalType.LATE);
        List<FlightInfo> onTime = getArrivalFlight(total, ArrivalType.ON_TIME);
        List<FlightInfo> before = getArrivalFlight(total, ArrivalType.BEFORE);

        return new PieChartModel(String.valueOf(total.size()), String.valueOf(late.size()), String.valueOf(onTime.size()), String.valueOf(before.size()));
    }

    @Override
    public CardChartVO getRealTimeChart(String quota) {
        Date currentTime = new Date();
        MonthTableNameHandler.setData(currentTime);
        LocalDateTime localDateTime = toLocalDateTime(currentTime);
        LocalDateTime twentyFourHoursAgo = localDateTime.minusHours(24);

        QueryWrapper<FlightInfo> wrapper = new QueryWrapper<>();
        wrapper.between("planned_departure_time", twentyFourHoursAgo, localDateTime);

//        if (Objects.equals(quota, "{\"quota\":\"visitors\"}")) {
//            wrapper.between("actual_departure_time", localDateTime.minusMinutes(30L), localDateTime.plusMinutes(30L));
//        } else if (Objects.equals(quota, "{\"quota\":\"published\"}")) {
//            wrapper.ge("is_cancel", 1);
//        }

        if (Objects.equals(quota, "{\"quota\":\"visitors\"}")) {
        List<XYChartVO> chartData = this.list(wrapper).stream()
                .filter(i -> !Objects.isNull(i.getActualDepartureTime()))
                .collect(Collectors.groupingBy(info -> {
                    // 获取actual_depature_time的小时部分
                    return info.getActualDepartureTime().getHours();
                }, Collectors.summingInt(info -> {
                    // 如果actual_depature_time为空，则返回0，否则返回1
                    return (info.getActualDepartureTime() != null) ? 1 : 0;
                })))
                .entrySet().stream()
                .map(entry -> new XYChartVO(
                        String.valueOf(entry.getKey()), // 小时
                        getHourlyTimeRange(entry.getKey()), // 小时对应的时间段
                        String.valueOf(entry.getValue()))) // 统计结果
                .collect(Collectors.toList());
            int count = chartData.stream()
                    .mapToInt(vo -> Integer.parseInt(vo.getY()))
                    .sum();

            return new CardChartVO(count, String.valueOf(Integer.parseInt(chartData.get(chartData.size() - 1).getY()) - Integer.parseInt(chartData.get(chartData.size() - 2).getY())), chartData);
        } else if (Objects.equals(quota, "{\"quota\":\"published\"}")) {
            List<XYChartVO> chartData = this.list(wrapper).stream()
                    .filter(i -> !Objects.isNull(i.getActualDepartureTime()))
                    .collect(Collectors.groupingBy(info -> {
                        // 获取actual_depature_time的小时部分
                        return info.getActualDepartureTime().getHours();
                    }, Collectors.summingInt(info -> {
                        // 如果actual_depature_time为空，则返回0，否则返回1
                        return (info.getIsCancel() == 1) ? 1 : 0;
                    })))
                    .entrySet().stream()
                    .map(entry -> new XYChartVO(
                            String.valueOf(entry.getKey()), // 小时
                            getHourlyTimeRange(entry.getKey()), // 小时对应的时间段
                            String.valueOf(entry.getValue()))) // 统计结果
                    .collect(Collectors.toList());

            int count = chartData.stream()
                    .mapToInt(vo -> Integer.parseInt(vo.getY()))
                    .sum();

            return new CardChartVO(count, String.valueOf(Integer.parseInt(chartData.get(chartData.size() - 1).getY()) - Integer.parseInt(chartData.get(chartData.size() - 2).getY())), chartData);
        } else if (Objects.equals(quota, "{\"quota\":\"comment\"}")) {
            List<XYChartVO> chartData = this.list(wrapper).stream()
                    .filter(i -> !Objects.isNull(i.getActualArrivalTime()))
                    .collect(Collectors.groupingBy(info -> {
                        // 获取actual_depature_time的小时部分
                        return info.getActualArrivalTime().getHours();
                    }, Collectors.summingInt(info -> {
                        // 如果actual_depature_time为空，则返回0，否则返回1
                        return (info.getActualArrivalTime() != null) ? 1 : 0;
                    })))
                    .entrySet().stream()
                    .map(entry -> new XYChartVO(
                            String.valueOf(entry.getKey()), // 小时
                            getHourlyTimeRange(entry.getKey()), // 小时对应的时间段
                            String.valueOf(entry.getValue()))) // 统计结果
                    .collect(Collectors.toList());
            int count = chartData.stream()
                    .mapToInt(vo -> Integer.parseInt(vo.getY()))
                    .sum();
            String increase = "";
            try {
                increase = String.valueOf(Integer.parseInt(chartData.get(chartData.size() - 1).getY()) - Integer.parseInt(chartData.get(chartData.size() - 2).getY()));
            } catch (Exception e) {
                increase = "0";
            }

            return new CardChartVO(count, increase, chartData);
        } else if (Objects.equals(quota, "{\"quota\":\"share\"}")) {
            List<FlightInfo> list = this.list(wrapper);
            List<XYChartVO> chartData = new ArrayList<>();


            int late = getArrivalFlight(list, ArrivalType.LATE).size();
            chartData.add(new XYChartVO("晚点" , "晚点" , String.valueOf(late)));

            int onTime = getArrivalFlight(list, ArrivalType.ON_TIME).size();
            chartData.add(new XYChartVO("准点" , "准点" , String.valueOf(onTime)));

            int before = getArrivalFlight(list, ArrivalType.BEFORE).size();
            chartData.add(new XYChartVO("提前" , "提前" , String.valueOf(before)));

            int today = chartData.size();
            int yesterday = getFlight(getDayFlight(getYesterday(currentTime)), FlightType.ARRIVED).size();
            return new CardChartVO(today, String.valueOf(today - yesterday), chartData);
        }
        return null;
    }

    @Override
    public List<RankingVO> getPopularFlight() {
        List<FlightInfo> todayFlight = this.getTodayFlight();
        AtomicInteger rank = new AtomicInteger(0);
        return todayFlight.stream()
                .collect(Collectors.groupingBy(FlightInfo::getFlightCode, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>, Long>comparing(entry -> entry.getValue()).reversed())
                .limit(10)
                .map(entry -> new RankingVO(
                        rank.incrementAndGet(),
                        entry.getKey(),
                        todayFlight.stream()
                                .filter(flightInfo -> entry.getKey().equals(flightInfo.getFlightCode()))
                                .findFirst()
                                .map(FlightInfo::getDepartureCity)
                                .orElse(null),
                        todayFlight.stream()
                                .filter(flightInfo -> entry.getKey().equals(flightInfo.getFlightCode()))
                                .findFirst()
                                .map(FlightInfo::getArrivalCity)
                                .orElse(null)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BarChartVO> getBarChart() {
        List<FlightInfo> todayFlight = getTodayFlight();

        return Arrays.stream(ArrivalType.values())
                .map(arrivalType -> {
                    List<Integer> counts = getHourLabels().stream()
                            .map(hourLabel -> getArrivalFlight(todayFlight, arrivalType).stream()
                                    .filter(flightInfo -> isFlightInHourRange(flightInfo, hourLabel))
                                    .collect(Collectors.toList()).size())
                            .collect(Collectors.toList());

                    return new BarChartVO(arrivalType.getMsg(), getHourLabels(), counts);
                })
                .collect(Collectors.toList());
    }

    @Override
    public LineChartVO getLineChart() {
        List<FlightInfo> todayFlight = getTodayFlight();

        List<String> xAxis = getHourLabels();
        List<DataVO> data = Arrays.stream(ArrivalType.values())
                .map(arrivalType -> {
                    List<Integer> values = xAxis.stream()
                            .map(hourLabel -> {
                                List<FlightInfo> filteredFlights = getArrivalFlight(todayFlight, arrivalType).stream()
                                        .filter(flightInfo -> isFlightInHourRange(flightInfo, hourLabel))
                                        .collect(Collectors.toList());
                                int count = filteredFlights.size();
                                int percentage = (int) ((count / (double) todayFlight.size()) * 100);
                                return count > 0 ? percentage : 0;
                            })
                            .collect(Collectors.toList());

                    return new DataVO(arrivalType.getMsg(), values);
                })
                .collect(Collectors.toList());

        return new LineChartVO(xAxis, data);
    }

    private List<String> getHourLabels() {
        int end = LocalDateTime.now().getHour();
        int start = 0;
        if (end > 12) {
            start = end - 12;
        } else {
            start = 24 - (12 - end);
        }
        return IntStream.rangeClosed(start, end)
                .mapToObj(hour -> String.format("%02d:00", hour))
                .collect(Collectors.toList());
    }

    private boolean isFlightInHourRange(FlightInfo flightInfo, String hourLabel) {
        String[] parts = hourLabel.split(":");
        int hour = Integer.parseInt(parts[0]);

        LocalTime flightDepartureTime = LocalTime.from(toLocalDateTime(flightInfo.getActualDepartureTime()));
        return flightDepartureTime.getHour() == hour;
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
                        .filter(flight -> {
                            LocalDateTime plannedArrival = toLocalDateTime(flight.getPlannedArrivalTime());
                            LocalDateTime actualArrival = toLocalDateTime(flight.getActualArrivalTime());
                            long minutesDifference = plannedArrival.until(actualArrival, java.time.temporal.ChronoUnit.MINUTES);
                            return minutesDifference < 0;
                        })
                        .collect(Collectors.toList());
            case BEFORE:
                return flightList.stream()
                        .filter(flight -> {
                            LocalDateTime plannedArrival = toLocalDateTime(flight.getPlannedArrivalTime());
                            LocalDateTime actualArrival = toLocalDateTime(flight.getActualArrivalTime());
                            long minutesDifference = plannedArrival.until(actualArrival, java.time.temporal.ChronoUnit.MINUTES);
                            return minutesDifference > 0;
                        })
                        .collect(Collectors.toList());
            case ON_TIME:
                return flightList.stream()
                        .filter(flight -> {
                            LocalDateTime plannedArrival = toLocalDateTime(flight.getPlannedArrivalTime());
                            LocalDateTime actualArrival = toLocalDateTime(flight.getActualArrivalTime());
                            long minutesDifference = plannedArrival.until(actualArrival, java.time.temporal.ChronoUnit.MINUTES);
                            return minutesDifference == 0;
                        })
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

    private LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private static String getHourlyTimeRange(int hour) {
        // 根据小时生成对应的时间段，这里可以根据实际情况进行调整
        return hour + ":00 - " + (hour + 1) + ":00";
    }
}





