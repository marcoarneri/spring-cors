package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.ReactClientRequest;
import it.krisopea.springcors.controller.model.ReactClientResponse;
import it.krisopea.springcors.service.ReactService;
import it.krisopea.springcors.service.dto.ReactClientResponseDto;
import it.krisopea.springcors.service.mapper.MapperAngularClientDto;
import it.krisopea.springcors.util.constant.PathConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstants.CLIENT_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ReactController {

    private final ReactService reactService;
    private final MapperAngularClientDto mapperAngularClientDto;

    @GetMapping(PathConstants.GET_CLIENTS)
    public ResponseEntity<List<ReactClientResponse>> getClients(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "4", required = false) int limit, @RequestParam String orderBy, @RequestParam String order) {
        Pair<List<ReactClientResponseDto>, Integer> getClientsResponseDto = reactService.getClients(page, limit, orderBy, order);
        Integer clientsSize = getClientsResponseDto.getRight();
        List<ReactClientResponse> users = mapperAngularClientDto.toClientResponse(getClientsResponseDto.getLeft());
        HttpHeaders headers = new HttpHeaders();
        headers.add("clientsSize", String.valueOf(clientsSize));
        return ResponseEntity.ok().headers(headers).body(users);
    }

//    @GetMapping(PathConstants.GET_CLIENTS)
//    public ResponseEntity<List<ReactClientResponse>> getClients() {
//        Pair<List<ReactClientResponseDto>, Integer> getClientsResponseDto = reactService.getClients();
//        Integer clientsSize = getClientsResponseDto.getRight();
//        List<ReactClientResponse> users = mapperAngularClientDto.toClientResponse(getClientsResponseDto.getLeft());
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("clientsSize", String.valueOf(clientsSize));
//        log.info("header clientsSize: [{}]", clientsSize);
//        return ResponseEntity.ok().headers(headers).body(users);
//    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ReactClientResponse> getClient(@PathVariable("id") Long id) {
        ReactClientResponseDto userDto = reactService.getClient(id);
        ReactClientResponse user = mapperAngularClientDto.toClientResponse(userDto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(PathConstants.ADD_CLIENT)
    public ResponseEntity<Void> addClient(@RequestBody @Valid ReactClientRequest request) throws InterruptedException {
        reactService.save(request);
        Thread.sleep(2500); //Per testare la progress lato frontend
//        throw new AppException(AppErrorCodeMessageEnum.ERROR); //Per testare l'alert di errore lato frontend
        return ResponseEntity.ok().build();
    }

    @PutMapping(PathConstants.UPDATE_CLIENT)
    public ResponseEntity<Void> updateClient(@RequestBody @Valid ReactClientRequest request){
        reactService.save(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id) {
        reactService.delete(id);
        return ResponseEntity.ok().build();
    }

}
