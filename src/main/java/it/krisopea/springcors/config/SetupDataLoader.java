package it.krisopea.springcors.config;

import static it.krisopea.springcors.util.constant.PrivilegeEnum.READ_PRIVILEGE;
import static it.krisopea.springcors.util.constant.PrivilegeEnum.WRITE_PRIVILEGE;

import it.krisopea.springcors.repository.PrivilegeRepository;
import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.PrivilegeEntity;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.util.constant.RoleConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  boolean alreadySetup = false;

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private PrivilegeRepository privilegeRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

    if (alreadySetup) return;
    PrivilegeEntity readPrivilege = createPrivilegeIfNotFound(String.valueOf(READ_PRIVILEGE));
    PrivilegeEntity writePrivilege = createPrivilegeIfNotFound(String.valueOf(WRITE_PRIVILEGE));

    List<PrivilegeEntity> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
    List<PrivilegeEntity> founderPrivileges = adminPrivileges;

    createRoleIfNotFound(RoleConstants.ROLE_ADMIN, adminPrivileges);
    createRoleIfNotFound(RoleConstants.ROLE_FOUNDER, founderPrivileges);
    createRoleIfNotFound(RoleConstants.ROLE_USER, Arrays.asList(readPrivilege));

    RoleEntity adminRole = roleRepository.findByName(RoleConstants.ROLE_ADMIN);
    UserEntity user = new UserEntity();
    user.setName("Test");
    user.setSurname("Test");
    user.setPassword(passwordEncoder.encode("test"));
    user.setEmail("test@test.com");
    user.setRoles(Collections.singletonList(adminRole));
    user.setEnabled(true);
    userRepository.save(user);

    alreadySetup = true;
  }

  @Transactional
  PrivilegeEntity createPrivilegeIfNotFound(String name) {

    PrivilegeEntity privilege = privilegeRepository.findByName(name);
    if (privilege == null) {
      privilege = new PrivilegeEntity();
      privilege.setName(name);
      privilegeRepository.save(privilege);
    }
    return privilege;
  }

  @Transactional
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
