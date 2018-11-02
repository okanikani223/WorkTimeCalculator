package org.kern.wtc;

import org.kern.wtc.interactor.WorkTimeCalcCommand;
import picocli.CommandLine;

public class App {

    public static void main (String... args) {
        CommandLine.call(new WorkTimeCalcCommand(), args);
    }
}