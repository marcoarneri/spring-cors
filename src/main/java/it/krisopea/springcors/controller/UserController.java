package it.krisopea.springcors.controller;


import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.response.UserResponse;
import it.krisopea.springcors.util.annotation.AnyRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Validated
@AnyRole
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @ModelAttribute("userLoginRequest") UserLoginRequest request){

        return ResponseEntity.ok().build();
    }
}
