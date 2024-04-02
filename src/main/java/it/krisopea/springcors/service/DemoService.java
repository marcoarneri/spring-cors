package it.krisopea.springcors.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.kafka.KafkaJsonProducer;
import it.krisopea.springcors.repository.DemoRepository;
import it.krisopea.springcors.repository.mapper.MapperDemoEntity;
import it.krisopea.springcors.repository.model.DemoEntity;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoService {

    @Autowired
    private KafkaJsonProducer kafkaJsonProducer;

    private final DemoRepository demoRepository;
    private final MapperDemoEntity mapperDemoEntity;

    public DemoResponseDto callDemoService(DemoRequestDto requestDto){

        validazioneSintattica(requestDto.getIuv());
        validazioneSemantica(requestDto.getIuv());

        DemoEntity entity = mapperDemoEntity.toEntity(requestDto);

        DemoEntity entitySaved = demoRepository.save(entity);
        log.info("Successfully saved entity: [{}]", entitySaved.toString());

//        Implementazione logica del servizio
//        Se tutto passa senza errori setto la risposta dto da tornare al controller
        DemoResponseDto responseDto = new DemoResponseDto();
        responseDto.setOutcome("OK");
        responseDto.setStatus("ELABORATO");

        try {
            kafkaJsonProducer.sendMessage(requestDto);
        } catch (JsonProcessingException e) {
            throw new AppException(AppErrorCodeMessageEnum.ERROR, e.getMessage());
        }

        return responseDto;
    }

    private void validazioneSemantica(String iuv) {
        //Implementazione validazione semantica e logica (a db)
        Optional<DemoEntity> byIuv = demoRepository.findByIuv(iuv);
        if(byIuv.isPresent()){
            throw new AppException(AppErrorCodeMessageEnum.IUV_DUPLICATE);
        }
    }

    private void validazioneSintattica(String iuv) {
        //Implementazione validazione sitattica e logica di validazione della request
    }
}
