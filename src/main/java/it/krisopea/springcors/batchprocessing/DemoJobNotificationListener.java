package it.krisopea.springcors.batchprocessing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class DemoJobNotificationListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job Started");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        log.info("Job Finished");
        try {
            Path source = Paths.get("src/main/resources/doc/demo.csv");

            Path processedDir = Paths.get("src/main/resources/doc/processed");
            if (!Files.exists(processedDir)) {
                Files.createDirectories(processedDir);
            }

            Path target = Paths.get("src/main/resources/doc/processed", source.getFileName().toString());

            Files.move(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
