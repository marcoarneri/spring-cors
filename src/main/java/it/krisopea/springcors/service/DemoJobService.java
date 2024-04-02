package it.krisopea.springcors.service;

import it.krisopea.springcors.repository.DemoRepository;
import it.krisopea.springcors.repository.mapper.MapperDemoEntity;
import it.krisopea.springcors.repository.model.DemoEntity;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoJobService {

    private final DemoRepository demoRepository;
    private final MapperDemoEntity mapperDemoEntity;


    public void jobService(DemoRequestDto requestDto){

        boolean validazioneSemantica = validazioneSemantica(requestDto.getIuv());

        if (!validazioneSemantica){
            return;
        }

        DemoEntity entity = mapperDemoEntity.toEntity(requestDto);

        DemoEntity entitySaved = save(entity);

        log.info("Successfully saved entity: [{}]", entitySaved);
    }

    private boolean validazioneSemantica(String iuv) {
        Optional<DemoEntity> byIuv = demoRepository.findByIuv(iuv);
        if(byIuv.isPresent()){
            log.error("Record con IUV duplicato [{}]", iuv);
            return false;
        }
        return true;
    }

    private DemoEntity save(DemoEntity entity){
        return demoRepository.saveAndFlush(entity);
    }


}
