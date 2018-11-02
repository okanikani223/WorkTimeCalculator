package org.kern.wtc.entity;

import lombok.Builder;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.LocalTime.NOON;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.*;

@Builder
public class Calculator {
    private static final List<DayOfWeek> HOLIDAYS = Arrays.asList(SATURDAY, SUNDAY);
    private static final LocalTime PM_WORK_START = LocalTime.of(13, 00);
    private static final Predicate<DayWorkInfo> isHoliDay = i -> HOLIDAYS.contains(i.getDay().getDayOfWeek());
    private static final Predicate<DayWorkInfo> isWeekDay = isHoliDay.negate();
    private static final BinaryOperator<Duration> sum = (prev, next) -> prev.plus(next);
    private boolean isRound;
    private Duration fixedWorkTime;
    private Duration durationOfBreakTime;
    private int amountOfRoundUnit;
    private List<LocalDateTime> workDays;

    private final Function<Map.Entry<LocalDate, List<LocalTime>>, DayWorkInfo> toDayWorkInfo = e -> {
        LocalTime arrive = isRound ? Rounder.of(e.getValue().stream().min(naturalOrder()).get(), amountOfRoundUnit).round()
                                   : e.getValue().stream().min(naturalOrder()).get();
        arrive = arrive.isAfter(NOON) && arrive.isBefore(PM_WORK_START) ? PM_WORK_START : arrive;

        LocalTime leave = isRound ? Rounder.of(e.getValue().stream().max(naturalOrder()).get(), amountOfRoundUnit).round()
                                  : e.getValue().stream().max(naturalOrder()).get();

        Duration workTime = arrive.isAfter(PM_WORK_START) ? Duration.between(arrive, leave) : Duration.between(arrive, leave).minus(durationOfBreakTime);
        Duration overTime = workTime.minus(fixedWorkTime);

        return DayWorkInfo.builder().day(e.getKey()).arrive(arrive).leave(leave).workTime(workTime).overTime(overTime).build();
    };

    public CalculatedResult calc () {
       Map<LocalDate, List<LocalTime>> groupedByDate = workDays.stream()
                                                               .collect(groupingBy(LocalDateTime::toLocalDate,
                                                                        mapping(LocalDateTime::toLocalTime, toList())));
       List<DayWorkInfo> workDays =  groupedByDate.entrySet().stream().map(toDayWorkInfo).collect(toList());

       TotalWorkInfo weekDay = total(workDays.stream().filter(isWeekDay).collect(toList()));
       TotalWorkInfo holiDay = total(workDays.stream().filter(isHoliDay).collect(toList()));

       return CalculatedResult.builder()
                              .days(workDays)
                              .weekDay(weekDay)
                              .holiDay(holiDay)
                              .build();
    }

    private TotalWorkInfo total(List<DayWorkInfo> works) {
        if (works.isEmpty()) {
            return TotalWorkInfo.empty();
        }

        return TotalWorkInfo.builder()
                            .amountOfWorkDayCount(works.size())
                            .totalWorkTime(works.stream().map(DayWorkInfo::getWorkTime).reduce(sum).get())
                            .totalOverTime(works.stream().map(DayWorkInfo::getOverTime).reduce(sum).get())
                            .build();
    }
}
