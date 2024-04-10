package it.krisopea.springcors.config;

import it.krisopea.springcors.repository.PrivilegeRepository;
import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.PrivilegeEntity;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static it.krisopea.springcors.util.constant.PrivilegeEnum.*;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  boolean alreadySetup = false;
  private final RoleRepository roleRepository;
  private final PrivilegeRepository privilegeRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {

    if (alreadySetup)
      return;

    PrivilegeEntity readPrivilege = createPrivilegeIfNotFound(String.valueOf(READ_PRIVILEGE));
    PrivilegeEntity writePrivilege = createPrivilegeIfNotFound(String.valueOf(WRITE_PRIVILEGE));
    PrivilegeEntity deletePrivilege = createPrivilegeIfNotFound(String.valueOf(DELETE_PRIVILEGE));

    List<PrivilegeEntity> userPrivileges = Collections.singletonList(readPrivilege);
    List<PrivilegeEntity> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
    List<PrivilegeEntity> founderPrivileges = Arrays.asList(readPrivilege, writePrivilege, deletePrivilege);

    createRoleIfNotFound(RoleConstants.ROLE_USER, userPrivileges);
    createRoleIfNotFound(RoleConstants.ROLE_ADMIN, adminPrivileges);
    createRoleIfNotFound(RoleConstants.ROLE_FOUNDER, founderPrivileges);

    RoleEntity adminRole = roleRepository.findByName(RoleConstants.ROLE_ADMIN);
    UserEntity user = new UserEntity();
    user.setName("Andrea");
    user.setSurname("Rossi");
    user.setUsername("rossian");
    user.setPassword(passwordEncoder.encode("password"));
    user.setEmail("andrea.rossi@test.com");
    user.setRoles(Collections.singletonList(adminRole));
    user.setEnabled(true);
    userRepository.save(user);

    alreadySetup = true;
  }

  PrivilegeEntity createPrivilegeIfNotFound(String name) {

    PrivilegeEntity privilege = privilegeRepository.findByName(name);
    if (privilege == null) {
      privilege = new PrivilegeEntity();
      privilege.setName(name);
      privilegeRepository.save(privilege);
    }
    return privilege;
  }

  RoleEntity createRoleIfNotFound(String name, Collection<PrivilegeEntity> privileges) {

    RoleEntity role = roleRepository.findByName(name);
    if (role == null) {
      role = new RoleEntity();
      role.setName(name);
      role.setPrivileges(privileges);
      roleRepository.save(role);
    }
    return role;
  }
}
