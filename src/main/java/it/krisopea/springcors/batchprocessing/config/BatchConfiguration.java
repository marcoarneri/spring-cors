package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.batchprocessing.DemoItemProcessor;
import it.krisopea.springcors.batchprocessing.DemoJobNotificationListener;
import it.krisopea.springcors.batchprocessing.JobRestarter;
import it.krisopea.springcors.batchprocessing.model.ErrorResposeWriter;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.DemoJobService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

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

    @Bean
    public Job demoJob(JobRepository jobRepository, Step startStep, Step restartStep, DemoJobNotificationListener listener) {
        return new JobBuilder("demoJob", jobRepository)
                .listener(listener)
                .start(startStep)
                .on("*").to(restartStep)
                .from(startStep).on("FAILED").to(restartStep)
                .from(startStep).on("UNKNOWN").to(restartStep)
                .end()
                .build();
    }

    @Bean
    public Step startStep(JobRestarter jobRestarter, JobRepository jobRepository, JpaTransactionManager transactionManager,
                          FlatFileItemReader<DemoRequest> reader, DemoItemProcessor processor,
                          ItemWriter<DemoRequestDto> writer) {
        return new StepBuilder("startStep", jobRepository)
                .<DemoRequest, DemoRequestDto> chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        try {
                            jobRestarter.startJob();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                })
//                .listener(new SkipListener<DemoRequest, DemoRequestDto>() {
//                    @Override
//                    public void onSkipInRead(Throwable t) {
//                        // Implementa la logica per gestire il record saltato durante la lettura
//                    }
//                })
//                .writer(errorWriter())
                .build();
    }

    @Bean
    public Step restartStep(JobRestarter jobRestarter, JobRepository jobRepository, JpaTransactionManager transactionManager,
                            FlatFileItemReader<DemoRequest> reader, DemoItemProcessor processor,
                            ItemWriter<DemoRequestDto> writer) {
        return new StepBuilder("restartStep", jobRepository)
                .<DemoRequest, DemoRequestDto>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        try {
                            jobRestarter.restartJob();
                        } catch (Exception e) {
                            e.printStackTrace(); // Gestisci l'eccezione in modo appropriato
                        }
                    }
                })
                .build();
    }

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

//    @Bean
//    public FlatFileItemWriter errorWriter(){
//        File outputFile = new File("src/main/resources/doc/error/error.csv");
//        if(!outputFile.exists()) {
//            try {
//                outputFile.createNewFile();
//            }catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return new FlatFileItemWriterBuilder<ErrorResposeWriter>()
//                .name("errorItemWriter")
//                .resource(new FileSystemResource(outputFile))
//                .lineAggregator(new DelimitedLineAggregator<>() {{
//                    setDelimiter(",");
//                    setFieldExtractor(new BeanWrapperFieldExtractor<>(){{
//                        setNames(new String[] {"record.iuv, record.city, record.nation, record.noticeId, errorMessage"});
//                    }});
//                }})
//                .headerCallback(writer -> writer.write("iuv, city, nation, noticeId, errorMessage"))
//                .build();
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
