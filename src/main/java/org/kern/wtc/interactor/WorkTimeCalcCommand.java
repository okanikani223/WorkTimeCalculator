package org.kern.wtc.interactor;

import org.kern.wtc.entity.CalculatedResult;
import org.kern.wtc.entity.Calculator;
import org.kern.wtc.usecase.ICommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Command
public class WorkTimeCalcCommand implements ICommand, Callable<Void> {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss");
    private static final Predicate<String> isDateTimeString = Pattern.compile("\\d{4}/\\d{2}/\\d{2} (?:\\d|\\d{2}):\\d{2}:\\d{2}").asPredicate();

    @Option(names = {"-r", "--round"}, description = "時間を丸める")
    private boolean isRound;
    @Option(names = {"-f", "--fixedTime"}, description = "固定の勤務時間を指定")
    private float fixedWorkTime;
    @Option(names = {"-b", "--breakTime"}, description = "休憩時間を指定")
    private int durationOfBreakTime;
    @Option(names = {"-u", "--roundUnit"}, description = "丸め単位を指定")
    private int amountOfRoundUnit;

    @Override
    public Void call() {
        exec(System.in);
        return null;
    }

    @Override
    public void exec(InputStream in) {
        List<String> lines =  new BufferedReader(new InputStreamReader(in)).lines().filter(isDateTimeString).collect(toList());
        List<LocalDateTime> works = lines.stream().map(line -> LocalDateTime.parse(line, DEFAULT_TIME_FORMATTER)).collect(toList());

        Calculator calculator = Calculator.builder()
                                          .isRound(isRound)
                                          .amountOfRoundUnit(amountOfRoundUnit)
                                          .fixedWorkTime(Duration.ofMinutes((long)(fixedWorkTime * 60)))
                                          .durationOfBreakTime(Duration.ofMinutes(durationOfBreakTime * 60))
                                          .workDays(works)
                                          .build();

        CalculatedResult result = calculator.calc();
    }

    private void printResult(CalculatedResult result) {
    }
}
