package it.krisopea.springcors.service;

import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.util.UuidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationService {
  private final VerificationRepository verificationRepository;

  public String getUsernameById(String id) {
    return verificationRepository
        .findById(UuidUtil.formatToken(id))
        .get()
        .getUserEntity()
        .getUsername();
  }

  public String getIdByUsername(String username) {
    return UuidUtil.removeDashesFromUuidString(
        verificationRepository.findByUsername(username).get().getId().toString());
  }
}
