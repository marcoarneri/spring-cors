package it.krisopea.springcors.batchprocessing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
            Path sourceDir = Paths.get("src/main/resources/doc/to-process");
            Path targetDir = Paths.get("src/main/resources/doc/processed/");

            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Files.walk(sourceDir)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(csvFile -> {
                        try {
                            Path targetFile = targetDir.resolve(csvFile.getFileName());
                            Files.move(csvFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
