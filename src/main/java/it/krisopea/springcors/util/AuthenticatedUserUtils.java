package it.krisopea.springcors.util;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticatedUserUtils {
  private final UserRepository userRepository;

  public Boolean hasId(UUID id) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<UserEntity> userEntity = userRepository.findByUsername(username);
    return userEntity.map(entity -> entity.getId().equals(id)).orElse(false);
  }
}
