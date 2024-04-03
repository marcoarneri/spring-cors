package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.annotation.AnyRole;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.USER_PATH)
@Slf4j
@Validated
@AnyRole
public class UserController {
    private final UserService userService;
    private final MapperUserDto mapperUserDto;

    @PutMapping("/update/{" + PathMappingConstants.USER_ID + "}")
    public ResponseEntity<Void> updateUser(
            @PathVariable UUID userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Update request for user with user ID: {}", userId);

        UserUpdateRequestDto userUpdateRequestDto = mapperUserDto.toUserUpdateDto(userUpdateRequest);
        userService.updateUser(userUpdateRequestDto, userId);

        log.info("Updated user with user ID: {}", userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{" + PathMappingConstants.USER_ID + "}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID userId, @Valid @RequestBody UserDeleteRequest deleteUserRequest) {
        log.info("Delete request with user ID: {}", userId);

        UserDeleteRequestDto userDeleteRequestDto = mapperUserDto.toUserDeleteDto(deleteUserRequest);
        userService.deleteUser(userDeleteRequestDto, userId);

        log.info("Deleted user with user ID: {}", userId);
        return ResponseEntity.ok().build();
    }
}
