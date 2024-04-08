package it.krisopea.springcors.batchprocessing;

import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@Slf4j
public class SkippedRecordListenerConf implements SkipListener<DemoRequest, DemoRequestDto> {

    private final List<DemoRequestDto> skippedRecords = new ArrayList<>();

    @Override
    public void onSkipInWrite(DemoRequestDto item, Throwable throwable) {
        log.info("ENTRATO NELLO SKIP LISTENER " + item);
        skippedRecords.add(item);
    }

}
