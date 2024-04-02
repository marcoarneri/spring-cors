package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.batchprocessing.DemoItemProcessor;
import it.krisopea.springcors.batchprocessing.DemoJobNotificationListener;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.DemoJobService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchConfiguration {

    private final DemoJobService demoJobService;

    @Autowired
    private DataSource dataSource;

    @Bean
    @Primary
    public JpaTransactionManager jpaTransactionManager() {
        final JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        return tm;
    }

    @Bean
    public Job demoJob(JobRepository jobRepository, Step step1, DemoJobNotificationListener listener) {
        return new JobBuilder("demoJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, JpaTransactionManager transactionManager,
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
