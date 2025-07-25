package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.config.JobConfig;

import java.util.Properties;

@RequiredArgsConstructor
@ShellComponent
public class ShellController {

    private final JobOperator jobOperator;

    private Integer launchCounter = 0;

    @ShellMethod(value = "startImportJob", key = "start")
    public void startImportAuthorsJob() throws Exception {
        Properties properties = new Properties();
        properties.put("launchId", (launchCounter++).toString());
        jobOperator.start(JobConfig.IMPORT_JOB_NAME, properties);
    }
}
