package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.batchprocessing.DemoItemProcessor;
import it.krisopea.springcors.batchprocessing.DemoJobNotificationListener;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.DemoJobService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final DemoJobService demoJobService;

    @Bean
    public Job demoJob(JobRepository jobRepository, Step step1, DemoJobNotificationListener listener) {
        return new JobBuilder("demoJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<DemoRequest> reader, DemoItemProcessor processor,
                      ItemWriter<DemoRequestDto> writer) {
        return new StepBuilder("step1", jobRepository)
                .<DemoRequest, DemoRequestDto> chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


    @Bean
    public FlatFileItemReader<DemoRequest> reader() {
        return new FlatFileItemReaderBuilder<DemoRequest>()
                .name("demoItemReader")
                .resource(new ClassPathResource("demo.csv"))
                .delimited()
                .names("iuv", "city", "nation", "noticeId")
                .targetType(DemoRequest.class)
                .build();
    }

    @Bean
    public DemoItemProcessor processor() {
        return new DemoItemProcessor();
    }

    @Bean
    public ItemWriter<DemoRequestDto> writer() {
        return records -> records.forEach(demoJobService::jobService);
    }
}
