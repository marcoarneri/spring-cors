package it.krisopea.springcors.batchprocessing;

import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.mapper.MapperDemoDto;
import lombok.Data;
import lombok.Getter;
import org.springframework.batch.core.SkipListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
public class SkippedRecordListenerConf implements SkipListener<DemoRequest, DemoRequestDto> {

    private final List<DemoRequestDto> skippedRecords = new ArrayList<>();

    @Override
    public void onSkipInWrite(DemoRequestDto item, Throwable throwable) {
        skippedRecords.add(item);
    }

}
