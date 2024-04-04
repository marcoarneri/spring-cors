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

//    private final Job demoJob;
//    private final JobLauncher jobLauncher;
//    private final JobExplorer jobExplorer;
//    private final JobOperator jobOperator;
//
//    @Scheduled(fixedDelay = 30000)
//    public void runBatchJob() {
//        try {
//            boolean isFirstRun = isFirstRun();
//
//            if (isFirstRun) {
//                JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
//                        .addLong("time", System.currentTimeMillis());
//                jobLauncher.run(demoJob, jobParametersBuilder.toJobParameters());
//                log.info("Job avviato per la prima volta.");
//            } else {
//
//                boolean isJobInterrupted = isJobInterrupted();
//                if (isJobInterrupted) {
//                    JobRestarter jobRestarter = new JobRestarter(jobExplorer, jobOperator);
//                    jobRestarter.restartJob();
//                }
//            }
//        } catch (Exception e) {
//            log.error("Errore durante l'esecuzione del job:", e);
//        }
//    }
//
//    public boolean isFirstRun() {
//        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
//        return jobInstance == null;
//    }
//
//    public boolean isJobInterrupted() {
//        // Ottieni l'ultima esecuzione del job
//        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
//        JobExecution lastExecution = jobExplorer.getLastJobExecution(jobInstance);
//
//        // Controlla se l'ultima esecuzione del job non è stata completata
//        return lastExecution != null && lastExecution.getStatus() == BatchStatus.FAILED;
//    }
}
