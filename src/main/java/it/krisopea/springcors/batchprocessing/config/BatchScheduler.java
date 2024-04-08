package it.krisopea.springcors.batchprocessing.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job demoJob;

    @Scheduled(fixedDelay = 30000)
    public void runBatchJob() {
            try {
                jobLauncher.run(demoJob, new JobParameters());
            } catch (Exception e) {
                log.error("Errore durante l'esecuzione del job:", e);
            }
    }
}
