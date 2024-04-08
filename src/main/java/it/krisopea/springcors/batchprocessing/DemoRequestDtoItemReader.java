package it.krisopea.springcors.batchprocessing;

import it.krisopea.springcors.service.dto.DemoRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
@Component
public class DemoRequestDtoItemReader implements ItemReader<DemoRequestDto>, InitializingBean {

    private Iterator<DemoRequestDto> iterator;
    @Autowired
    private SkippedRecordListenerConf skipListener;

    @Override
    public DemoRequestDto read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("ENTRATO NEL READ DEL SECONDO STEP");
        if (this.iterator == null){
            this.iterator = skipListener.getSkippedRecords().iterator();
        }
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null; // Return null when all items have been read
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
