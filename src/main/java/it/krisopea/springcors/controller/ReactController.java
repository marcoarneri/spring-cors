package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.ReactClientRequest;
import it.krisopea.springcors.controller.model.ReactClientResponse;
import it.krisopea.springcors.service.ReactService;
import it.krisopea.springcors.service.dto.ReactClientResponseDto;
import it.krisopea.springcors.service.mapper.MapperAngularClientDto;
import it.krisopea.springcors.util.constant.PathConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstants.CLIENT_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class ReactController {

    private final ReactService reactService;
    private final MapperAngularClientDto mapperAngularClientDto;

    @GetMapping(PathConstants.GET_CLIENTS)
    public ResponseEntity<List<ReactClientResponse>> getClients(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "4", required = false) int limit) {
        int currentPage = page - 1;
        List<ReactClientResponseDto> usersDto = reactService.getClients(currentPage, limit);
        List<ReactClientResponse> users = mapperAngularClientDto.toClientResponse(usersDto);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping(PathConstants.GET_CLIENTS_SIZE)
    public ResponseEntity<Integer> getClientsSize() {
        Integer clientsSize = reactService.getClientsSize();
        return ResponseEntity.ok().body(clientsSize);
    }

//    @GetMapping(PathConstants.GET_CLIENTS)
//    public ResponseEntity<List<ReactClientResponse>> getClients() {
//        List<ReactClientResponseDto> usersDto = reactService.getClients();
//        List<ReactClientResponse> users = mapperAngularClientDto.toClientResponse(usersDto);
//        return ResponseEntity.ok().body(users);
//    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ReactClientResponse> getClient(@PathVariable("id") Long id) {
        ReactClientResponseDto userDto = reactService.getClient(id);
        ReactClientResponse user = mapperAngularClientDto.toClientResponse(userDto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(PathConstants.ADD_CLIENT)
    public ResponseEntity<Void> addClient(@RequestBody ReactClientRequest request) {
        reactService.save(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(PathConstants.UPDATE_CLIENT)
    public ResponseEntity<Void> updateClient(@RequestBody ReactClientRequest request){
        reactService.save(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id) {
        reactService.delete(id);
        return ResponseEntity.ok().build();
    }

}
