package it.krisopea.springcors.batchprocessing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRestarter {

    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;
    private final Job demoJob;
    private final JobLauncher jobLauncher;


    public void restartOrStartJobIfFirstRun() throws Exception {
        if (isFirstRun()) {
            startJob();
        } else if (isJobInterrupted()) {
            restartJob();
        }else {
            log.info("Non ci sono altri record da aggiungere.");
        }
    }

    private boolean isFirstRun() {
        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
        return jobInstance == null;
    }

    private boolean isJobInterrupted() {
        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
        JobExecution lastExecution = jobExplorer.getLastJobExecution(jobInstance);
        return lastExecution != null && lastExecution.getStatus() == BatchStatus.FAILED;
    }

    private void startJob() throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobLauncher.run(demoJob, jobParametersBuilder.toJobParameters());
        log.info("Job avviato per la prima volta.");
    }

    private void restartJob() throws Exception {
        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
        JobExecution lastExecution = jobExplorer.getLastJobExecution(jobInstance);
        Long lastExecutionId = lastExecution.getId();
        jobOperator.restart(lastExecutionId);
    }
}
