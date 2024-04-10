package it.krisopea.springcors.config;

import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.PrivilegeEntity;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.util.constant.RoleConstants;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  //  @Autowired private IUserService service; TODO

  //  @Autowired private MessageSource messages;

  @Autowired private RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity user = userRepository.findByUsername(username).get();

    if (user == null) {
      return new User(
          " ",
          " ",
          true,
          true,
          true,
          true,
          getAuthorities(
              Collections.singletonList(roleRepository.findByName(RoleConstants.ROLE_USER))));
    }

    return new User(
        user.getUsername(),
        user.getPassword(),
        user.isEnabled(),
        true,
        true,
        true,
        getAuthorities(user.getRoles()));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {
    return getGrantedAuthorities(getPrivileges(roles));
  }

  private List<String> getPrivileges(Collection<RoleEntity> roles) {

    List<String> privileges = new ArrayList<>();
    List<PrivilegeEntity> collection = new ArrayList<>();

    for (RoleEntity role : roles) {
      privileges.add(role.getName());
      collection.addAll(role.getPrivileges());
    }

    for (PrivilegeEntity item : collection) {
      privileges.add(item.getName());
    }
    return privileges;
  }

  private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    for (String privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege));
    }
    return authorities;
  }
}
