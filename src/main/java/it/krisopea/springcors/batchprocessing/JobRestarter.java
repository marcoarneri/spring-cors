package it.krisopea.springcors.batchprocessing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRestarter {

    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;

    public void restartJob() throws Exception {

        JobInstance jobInstance = jobExplorer.getLastJobInstance("demoJob");
        JobExecution lastExecution = jobExplorer.getLastJobExecution(jobInstance);

        if  (lastExecution != null && lastExecution.getStatus() == BatchStatus.FAILED) {
            Long lastExecutionId = jobExplorer.getLastJobExecution(jobInstance).getId();


            jobOperator.restart(lastExecutionId);
        } else {
            log.info("Non ci sono nuovi dati da salvare.");
        }
    }
}
