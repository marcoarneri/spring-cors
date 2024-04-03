package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class TemplateController {

    private final UserService userService;
    private final MapperUserDto userMapperDto;

    @GetMapping("/register")
    public ModelAndView showRegistrationForm(ModelMap model) {
        model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        return new ModelAndView("register", model);
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("userRegistrationRequest") @Valid UserRegistrationRequest request, ModelMap model) {

        UserRegistrationRequestDto requestDto =
                userMapperDto.toUserRegistrationRequestDto(request);
        userService.registerUser(requestDto);
        model.addAttribute("registrationSuccess", true);
        model.addAttribute("message", "Registrazione completata con successo!");
        return new ModelAndView("home");
    }

}
