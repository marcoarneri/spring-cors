package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.annotation.AnyRole;
import it.krisopea.springcors.util.annotation.IsAdmin;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.ADMIN_PATH)
@Slf4j
@Validated
@IsAdmin
public class AdminController {
    private final UserService userService;
    private final MapperUserDto mapperUserDto;
    @DeleteMapping("/delete/{" + PathMappingConstants.USER_ID + "}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("Admin's delete request with user ID: {}", userId);

        userService.deleteUser(userId);

        log.info("Admin deleted user with user ID: {}", userId);
        return ResponseEntity.ok().build();
    }
}
