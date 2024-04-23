package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.AngularClientRequest;
import it.krisopea.springcors.controller.model.AngularClientResponse;
import it.krisopea.springcors.service.AngularService;
import it.krisopea.springcors.service.dto.AngularClientResponseDto;
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
public class AngularController {

    private final AngularService angularService;
    private final MapperAngularClientDto mapperAngularClientDto;

    @GetMapping(PathConstants.GET_CLIENTS)
    public ResponseEntity<List<AngularClientResponse>> getClients() {
        List<AngularClientResponseDto> usersDto = angularService.getClients();
        List<AngularClientResponse> users = mapperAngularClientDto.toClientResponse(usersDto);
        return ResponseEntity.ok().body(users);
    }

    @PostMapping(PathConstants.ADD_CLIENT)
    public ResponseEntity<Void> addClient(@RequestBody AngularClientRequest request) {
        angularService.save(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(PathConstants.UPDATE_CLIENT)
    public ResponseEntity<Void> updateClient(@RequestBody AngularClientRequest request){
        angularService.save(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id) {
        angularService.delete(id);
        return ResponseEntity.ok().build();
    }

}
