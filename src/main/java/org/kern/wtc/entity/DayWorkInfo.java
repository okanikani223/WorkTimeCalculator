package org.kern.wtc.entity;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class DayWorkInfo {
    private LocalDate day;
    private LocalTime arrive;
    private LocalTime leave;
    private Duration workTime;
    private Duration overTime;
}
