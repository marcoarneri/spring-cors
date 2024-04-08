package it.krisopea.springcors.batchprocessing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

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
        //TODO: implementare parte di spostamento file errore
    }
}
