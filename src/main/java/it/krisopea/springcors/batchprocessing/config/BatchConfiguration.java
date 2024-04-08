package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.batchprocessing.DemoItemProcessor;
import it.krisopea.springcors.batchprocessing.DemoJobNotificationListener;
import it.krisopea.springcors.batchprocessing.DemoRequestDtoItemReader;
import it.krisopea.springcors.batchprocessing.SkippedRecordListenerConf;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.DemoJobService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
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

    @Bean
    public Job demoJob(JobRepository jobRepository, Step startStep, Step errorHandlingStep, DemoJobNotificationListener listener) {
        return new JobBuilder("demoJob", jobRepository)
                .listener(listener)
                .start(startStep)
                .next(errorHandlingStep)
                .build();
    }

    @Bean
    public Step startStep(JobRepository jobRepository, JpaTransactionManager transactionManager,
                          DemoItemProcessor processor,
                          ItemWriter<DemoRequestDto> writer, SkippedRecordListenerConf skipListener) throws IOException {
        return new StepBuilder("startStep", jobRepository)
                .<DemoRequest, DemoRequestDto> chunk(1, transactionManager)
                .reader(multiResourceItemReader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy((t, skipCount) -> true)
                .listener(skipListener)
                .build();
    }

    @Bean
    public Step errorHandlingStep(JobRepository jobRepository, JpaTransactionManager transactionManager,
                                  @Qualifier("errorWriter") FlatFileItemWriter<DemoRequestDto> errorWriter) {
        return new StepBuilder("errorHandlingStep", jobRepository)
                .<DemoRequestDto, DemoRequestDto>chunk(1, transactionManager)
                .reader(demoRequestDtoItemReader())
                .writer(errorWriter)
                .faultTolerant()
                .skipPolicy((t, skipCount) -> true)
                .build();
    }


    @Bean
    public FlatFileItemReader<DemoRequest> reader() {
        log.info("ENTRATO NEL READER DEL PRIMO STEP");
        return new FlatFileItemReaderBuilder<DemoRequest>()
                .name("demoItemReader")
                .resource(new ClassPathResource("doc/to-process/demo.csv"))
                .delimited()
                .names("iuv", "city", "nation", "noticeId")
                .linesToSkip(1)
                .targetType(DemoRequest.class)
                .build();
    }

    @Bean
    public FlatFileItemReader<DemoRequest> flatFileItemReader() {
        FlatFileItemReader<DemoRequest> reader = new FlatFileItemReader<>();
        reader.setLineMapper(new DefaultLineMapper<DemoRequest>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"iuv", "city", "nation", "noticeId"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<DemoRequest>() {{
                setTargetType(DemoRequest.class);
            }});
        }});
        reader.setLinesToSkip(1);
        return reader;
    }

    @Bean
    public MultiResourceItemReader<DemoRequest> multiResourceItemReader() throws IOException {
        Resource[] resources = loadCsvFilesFromDirectory("doc/to-process");

        MultiResourceItemReader<DemoRequest> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setResources(resources);
        multiResourceItemReader.setDelegate(flatFileItemReader()); // Usa il reader configurato sopra come delegate
        return multiResourceItemReader;
    }

    private Resource[] loadCsvFilesFromDirectory(String directoryPath) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:" + directoryPath + "/*.csv");
        return resources;
    }

    @Bean
    public DemoRequestDtoItemReader demoRequestDtoItemReader() {
        log.info("ENTRATO IN LIST ITEM READER" );
        return new DemoRequestDtoItemReader();
    }

    @Bean
    public DemoItemProcessor processor() {
        log.info("ENTRATO NEL PROCESSOR DEL PRIMO STEP");
        return new DemoItemProcessor();
    }

    @Bean
    public ItemWriter<DemoRequestDto> writer() {
        log.info("ENTRATO NEL WRITER DEL PRIMO STEP");
        return records -> records.forEach(demoJobService::jobService);
    }
}
