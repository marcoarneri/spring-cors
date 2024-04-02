package it.krisopea.springcors.batchprocessing;


import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.mapper.MapperDemoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoItemProcessor implements ItemProcessor<DemoRequest, DemoRequestDto> {
    @Autowired
    private MapperDemoDto mapperDemoDto;

    @Override
    public DemoRequestDto process(final DemoRequest demoRequest) {
        log.info("Processando il record csv in demoRequest.");
        return mapperDemoDto.toRequestDto(demoRequest);
    }
}
