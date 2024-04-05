//package it.krisopea.springcors.batchprocessing;
//
//
//import it.krisopea.springcors.batchprocessing.model.ErrorResponseWriter;
//import it.krisopea.springcors.controller.model.DemoRequest;
//import it.krisopea.springcors.service.dto.DemoRequestDto;
//import it.krisopea.springcors.service.mapper.MapperDemoDto;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class ErrorItemProcessor implements ItemProcessor<DemoRequestDto, ErrorResponseWriter> {
//    @Autowired
//    private MapperDemoDto mapperDemoDto;
//
//    @Override
////    public ErrorResponseWriter process(final DemoRequestDto requestDto) {
//        try{
//
//        }catch (Exception e) {
//            String exceptionMessage = e.getMessage();
//            return mapperDemoDto.demoRequestDtoToErrorResponseWriter(requestDto, exceptionMessage);
//        }
//        return null;
//    }
//}
