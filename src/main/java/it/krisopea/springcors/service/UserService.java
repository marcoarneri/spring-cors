package it.krisopea.springcors.service;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserEntity saveUser(OAuth2AuthenticationToken authenticationToken) {
    return userRepository.saveAndFlush(toUserEntity(authenticationToken));
  }

  public UserEntity toUserEntity(OAuth2AuthenticationToken token){
    UserEntity userEntity = new UserEntity();
    userEntity.setName(token.getPrincipal().getAttributes().get("given_name").toString());
    userEntity.setSurname(token.getPrincipal().getAttributes().get("family_name").toString());
    userEntity.setEmail(token.getPrincipal().getAttributes().get("email").toString());
    userEntity.setPicture(token.getPrincipal().getAttributes().get("picture").toString());
    userEntity.setRole("USER");
    return userEntity;
  }
}
