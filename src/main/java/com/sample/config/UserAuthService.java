package com.sample.config;

import com.sample.entity.User;
import com.sample.exception.UserNotFoundException;
import com.sample.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserAuthService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserAuthService(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UserNotFoundException {
    User user = userRepository.findByUserName(userName);
    if (user == null) {
      throw new UserNotFoundException("UserName not found");
    }
    return new org.springframework.security.core.userdetails.User(user.getUserName(),
        user.getPassword(), new ArrayList<>());
  }
}
