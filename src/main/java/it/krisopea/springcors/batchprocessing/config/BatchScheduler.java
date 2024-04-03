package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.batchprocessing.JobRestarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {

    private final Job demoJob;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;

    @Scheduled(fixedDelay = 30000)
    public void runBatchJob() {
        try {
            boolean isFirstRun = isFirstRun();

            if (isFirstRun) {
                JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
                jobLauncher.run(demoJob, jobParametersBuilder.toJobParameters());
                log.info("Job avviato per la prima volta.");
            } else {
                JobRestarter jobRestarter = new JobRestarter(jobExplorer,jobOperator);
                jobRestarter.restartJob();
            }
        } catch (Exception e) {
            log.error("Errore durante l'esecuzione del job:", e);
        }
    }

    public boolean isFirstRun() {
        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
        return jobInstance == null;
    }
}
