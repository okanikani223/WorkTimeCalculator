package org.kern.wtc.entity;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
public class CalculatedResult {
    private List<DayWorkInfo> days;
    private TotalWorkInfo weekDay;
    private TotalWorkInfo holiDay;

    public static CalculatedResult empty () {
        return CalculatedResult.builder()
                               .days(emptyList())
                               .weekDay(TotalWorkInfo.empty())
                               .holiDay(TotalWorkInfo.empty())
                               .build();
    }
}
