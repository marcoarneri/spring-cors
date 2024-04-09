package it.krisopea.springcors.service;

import it.krisopea.springcors.repository.DemoRepository;
import it.krisopea.springcors.repository.model.DemoEntity;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoRepository demoRepository;

    public DemoResponseDto callDemoService(DemoRequestDto requestDto){

        validazioneSintattica(requestDto.getIuv());
        validazioneSemantica(requestDto.getIuv());

        if (demoRepository.duplicateIuv(requestDto.getIuv())) {
            // Gestisci il caso in cui la stringa iuv non è unica
            DemoResponseDto responseDto = new DemoResponseDto();
            responseDto.setOutcome("Error");
            responseDto.setStatus("Iuv duplicato");
            return responseDto;
        }

        DemoEntity pojo = new DemoEntity();
        pojo.setIuv(requestDto.getIuv());
        pojo.setLocation(requestDto.getLocation());
        pojo.setNoticeId(requestDto.getNoticeId());

        demoRepository.insert(pojo);

//        Implementazione logica del servizio
//        Se tutto passa senza errori setto la risposta dto da tornare al controller
        DemoResponseDto responseDto = new DemoResponseDto();
        responseDto.setOutcome("OK");
        responseDto.setStatus("ELABORATO");
        return responseDto;
    }

    private void validazioneSemantica(String iuv) {
        //Implementazione validazione semantica e logica (a db)
    }

    private void validazioneSintattica(String iuv) {
        //Implementazione validazione sitattica e logica di validazione della request
    }
}
