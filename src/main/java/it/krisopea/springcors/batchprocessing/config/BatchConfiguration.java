package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.batchprocessing.DemoItemProcessor;
import it.krisopea.springcors.batchprocessing.DemoJobNotificationListener;
//import it.krisopea.springcors.batchprocessing.JobRestarter;
//import it.krisopea.springcors.batchprocessing.model.ErrorResponseWriter;
import it.krisopea.springcors.batchprocessing.SkippedRecordListenerConf;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.DemoJobService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.awt.event.ItemListener;

import static it.krisopea.springcors.batchprocessing.config.ErrorWriterConfiguration.errorWriter;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final DemoJobService demoJobService;
    private final DataSource dataSource;

    @Bean(name = "transactionManager")
    @Primary
    public JpaTransactionManager jpaTransactionManager() {
        final JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        return tm;
    }

    //todo reitegrare lo step del restart e fare in modo che i record skippati vengano scritti sul file
    @Bean
    public Job demoJob(JobRepository jobRepository, Step startStep, Step errorHandlingStep, DemoJobNotificationListener listener) {
        return new JobBuilder("demoJob", jobRepository)
                .listener(listener)
                .start(startStep)
                .on("*").to(errorHandlingStep)
//                .from(startStep).on("*").to(errorHandlingStep)
//                .from(startStep).on("UNKNOWN").to(errorHandlingStep)
                .end()
                .build();
    }

    @Bean
    public Step startStep(JobRepository jobRepository, JpaTransactionManager transactionManager,
                          FlatFileItemReader<DemoRequest> reader, DemoItemProcessor processor,
                          ItemWriter<DemoRequestDto> writer, SkippedRecordListenerConf skipListener) {
        return new StepBuilder("startStep", jobRepository)
                .<DemoRequest, DemoRequestDto> chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy((t, skipCount) -> true)
                .listener(skipListener)
                .build();
    }

    @Bean
    public Step errorHandlingStep(JobRepository jobRepository, JpaTransactionManager transactionManager,
                                  SkippedRecordListenerConf skipListener) {
        return new StepBuilder("errorHandlingStep", jobRepository)
                .<DemoRequestDto, DemoRequestDto>chunk(1, transactionManager)
                .reader(new ListItemReader<>(skipListener.getSkippedRecords()))
                .writer(errorWriter())
                .build();
    }


//    @Bean
//    public Step restartStep(JobRestarter jobRestarter, JobRepository jobRepository, JpaTransactionManager transactionManager,
//                            FlatFileItemReader<DemoRequest> reader, DemoItemProcessor processor,
//                            ItemWriter<DemoRequestDto> writer) {
//        return new StepBuilder("restartStep", jobRepository)
//                .<DemoRequest, DemoRequestDto>chunk(1, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .listener(new StepExecutionListener() {
//                    @Override
//                    public void beforeStep(StepExecution stepExecution) {
//                        try {
//                            jobRestarter.restartJob();
//                        } catch (Exception e) {
//                            e.printStackTrace(); // Gestisci l'eccezione in modo appropriato
//                        }
//                    }
//                })
//                .build();
//    }

    @Bean
    public FlatFileItemReader<DemoRequest> reader() {
        return new FlatFileItemReaderBuilder<DemoRequest>()
                .name("demoItemReader")
                .resource(new ClassPathResource("doc/demo.csv"))
                .delimited()
                .names("iuv", "city", "nation", "noticeId")
                .linesToSkip(1)
                .targetType(DemoRequest.class)
                .build();
    }



    //TODO capire cosa fare per ClassifierCompositeItemWriter in modo da poter usare due writer differenti all'interno di uno step e capire che tipo di oggetto devo passare avendone due differenti
//    public ClassifierCompositeItemWriter<DemoRequestDto> classifierCompositeItemWriter(
//            ItemWriter<DemoRequestDto> primaryWriter,
//            FlatFileItemWriter<ErrorResposeWriter> errorWriter) {
//
//        ClassifierCompositeItemWriter<DemoRequestDto> compositeItemWriter = new ClassifierCompositeItemWriter<>();
//        compositeItemWriter.setClassifier((Classifier<DemoRequestDto, ItemWriter<? super DemoRequestDto>>) demoRequestDto -> {
//            // Determina a quale writer inviare il record in base a qualche criterio
//            if (demoRequestDto.getSomeCriteria()) {
//                return primaryWriter; // Invia il record al writer principale
//            } else {
//                return errorWriter; // Invia il record al writer degli errori
//            }
//        });
//        return compositeItemWriter;
//    }

    @Bean
    public DemoItemProcessor processor() {
        return new DemoItemProcessor();
    }

    @Bean
    public ItemWriter<DemoRequestDto> writer() {
        return records -> records.forEach(demoJobService::jobService);
    }
}
