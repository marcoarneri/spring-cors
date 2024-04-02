package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.DemoUserEntity;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import it.krisopea.springcors.service.dto.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoService {

    private final UserRepository userRepository;
    private final MapperUserEntity mapperUserEntity;

    public DemoResponseDto callDemoService(UserLoginRequestDto requestDto){

        validazioneSintattica(requestDto.getIuv());
        validazioneSemantica(requestDto.getIuv());

        DemoUserEntity entity = mapperUserEntity.toEntity(requestDto);

        DemoUserEntity entitySaved = userRepository.save(entity);
        log.info("Successfully saved entity: [{}]", entitySaved.toString());

//        Implementazione logica del servizio
//        Se tutto passa senza errori setto la risposta dto da tornare al controller
        DemoResponseDto responseDto = new DemoResponseDto();
        responseDto.setOutcome("OK");
        responseDto.setStatus("ELABORATO");
        return responseDto;
    }

    private void validazioneSemantica(String iuv) {
        //Implementazione validazione semantica e logica (a db)
        Optional<DemoUserEntity> byIuv = userRepository.findByIuv(iuv);
        if(byIuv.isPresent()){
            throw new AppException(AppErrorCodeMessageEnum.IUV_DUPLICATE);
        }
    }

    private void validazioneSintattica(String iuv) {
        //Implementazione validazione sitattica e logica di validazione della request
    }
}
