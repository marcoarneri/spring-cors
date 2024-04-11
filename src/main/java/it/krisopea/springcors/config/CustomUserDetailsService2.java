//package it.krisopea.springcors.config;
//
//import it.krisopea.springcors.repository.UserRepository;
//import it.krisopea.springcors.repository.model.PrivilegeEntity;
//import it.krisopea.springcors.repository.model.RoleEntity;
//import it.krisopea.springcors.repository.model.UserEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Service("userDetailsService")
//@Transactional
//@RequiredArgsConstructor
//public class CustomUserDetailsService2 implements UserDetailsService {
//
//  private final UserRepository userRepository;
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//    UserEntity user =
//        userRepository
//            .findByUsername(username)
//            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
//
//    return buildUserDetails(user);
//  }
//
//  private UserDetails buildUserDetails(UserEntity user) {
//    return User.builder()
//            .username(user.getUsername())
//            .password(user.getPassword())
//            .authorities(getAuthorities(user.getRoles()))
//            .roles(getRoles(user.getRoles()))
//            .accountExpired(!user.isEnabled())
//            .accountLocked(!user.isEnabled())
//            .credentialsExpired(!user.isEnabled())
//            .disabled(!user.isEnabled())
//            .build();
//  }
//
//  private Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {
//    return getGrantedAuthorities(getPrivileges(roles));
//  }
//
//  private String getRoles(Collection<RoleEntity> roles) {
//      String roleName = null;
//      for (RoleEntity role : roles) {
//          roleName = role.getName();
//      }
//      return roleName;
//  }
//
//  private List<String> getPrivileges(Collection<RoleEntity> roles) {
//
//    List<String> privileges = new ArrayList<>();
//    List<PrivilegeEntity> collection = new ArrayList<>();
//
//    for (RoleEntity role : roles) {
//      collection.addAll(role.getPrivileges());
//    }
//
//    for (PrivilegeEntity item : collection) {
//      privileges.add(item.getName());
//    }
//    return privileges;
//  }
//
//  private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
//    List<GrantedAuthority> authorities = new ArrayList<>();
//
//    for (String privilege : privileges) {
//      authorities.add(new SimpleGrantedAuthority(privilege));
//    }
//    return authorities;
//  }
//}
