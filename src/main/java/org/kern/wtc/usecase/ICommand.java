package org.kern.wtc.usecase;

import org.kern.wtc.entity.CalculatedResult;

import java.io.InputStream;

public interface ICommand {
    void exec(InputStream in);
}
