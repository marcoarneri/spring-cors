package it.krisopea.springcors.batchprocessing.config;

import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class ErrorWriterConfiguration {

    @Bean
    public FlatFileItemWriter<DemoRequestDto> errorWriter() {
        log.info("ENTRATO NEL FLATFILEWRITER");
        File outputFile = new File("src/main/resources/doc/error/error.csv");
        if (!outputFile.exists()) {
            try {
            outputFile.createNewFile();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new FlatFileItemWriterBuilder<DemoRequestDto>()
                .name("errorItemWriter")
                .resource(new FileSystemResource(outputFile))
                .lineAggregator(new DelimitedLineAggregator<>() {
                    {
                        setDelimiter(",");
                        setFieldExtractor(new BeanWrapperFieldExtractor<DemoRequestDto>() {
                            {
                                setNames(new String[]{"iuv", "location", "noticeId"});
                            }
                        });
                    }
                })
                .headerCallback(writer -> writer.write("iuv, location, noticeId"))
                .encoding(StandardCharsets.UTF_8.name())
                .build();
    }
}
