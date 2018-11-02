package org.kern.wtc.entity;


import java.time.LocalTime;

public class Rounder {

    private static final int amountOfLimitSeconds = LocalTime.MAX.toSecondOfDay();
    private LocalTime roundee;
    private int amountOfRoundUnit;

    private Rounder(LocalTime roundee, int amountOfRoundUnit) {
        this.roundee = roundee;
        this.amountOfRoundUnit = amountOfRoundUnit;
    }

    public static Rounder of (LocalTime roundee, int amountOfRoundUnit) {
        return new Rounder(roundee, amountOfRoundUnit);
    }

    /**
     * 渡された時間を丸め単位で丸める。
     * 計算式：((時間 + 調整分) / 丸め単位) * 丸め単位
     * @return 丸め済み時間
     */
    public LocalTime round() {
        int amountOfRound = amountOfRoundUnit * 60;
        int amountOfAdjust = (amountOfRound / 2) - 1;
        int adjustedSeconds = roundee.toSecondOfDay() + amountOfAdjust;

        // 調整の結果、次の日になった場合は上限の時間(23:59)を返す。
        if (adjustedSeconds > amountOfLimitSeconds) {
            return LocalTime.of(23, 59);
        }

        return LocalTime.ofSecondOfDay(((adjustedSeconds) / amountOfRound) * amountOfRound);
    }
}
