package org.kern.wtc.entity;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;

import static java.time.Duration.ZERO;

@Value
@Builder
public class TotalWorkInfo {

    private int amountOfWorkDayCount;
    private Duration totalWorkTime;
    private Duration totalOverTime;

    public static TotalWorkInfo empty () {
        return TotalWorkInfo.builder()
                            .amountOfWorkDayCount(0)
                            .totalWorkTime(ZERO)
                            .totalOverTime(ZERO)
                            .build();
    }
}
