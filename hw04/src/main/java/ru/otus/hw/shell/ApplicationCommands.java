package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Test Service Application")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService ioService;

    @ShellMethod(value = "Start Test Service", key = {"s", "start"})
    public String startTest() {
        testRunnerService.run();
        ioService.printLineLocalized("ApplicationCommands.application.finished");
        return "";
    }
}
