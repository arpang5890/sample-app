package com.sample.service;

import com.sample.entity.User;
import com.sample.exception.InsuffientFundsException;
import com.sample.exception.UserAlreadyExistsException;
import com.sample.exception.UserNotFoundException;
import com.sample.generated.model.v1.ApiCreateUser;
import com.sample.generated.model.v1.ApiTransfer;
import com.sample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final Long joinLockInTimeMins;
  private final Long balanceToAddOnJoining;

  @Autowired
  public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository,
      final @Value("${app.join.amount}") Long balanceToAddOnJoining,
      final @Value("${app.join.reset}") Long joinLockInTimeMins) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.balanceToAddOnJoining = balanceToAddOnJoining;
    this.joinLockInTimeMins = joinLockInTimeMins;
  }

  public void createUser(final ApiCreateUser apiCreateUser) {
    if (userRepository.findByUserName(apiCreateUser.getUserName()) != null) {
      throw new UserAlreadyExistsException("User already exists");
    }
    User userToCreate = toUser(apiCreateUser);
    userRepository.save(userToCreate);
  }

  public void joinFaucet(final String userName) {
    LocalDateTime shouldJoinBefore = LocalDateTime.now().minusMinutes(joinLockInTimeMins);
    userRepository.addBalance(userName, balanceToAddOnJoining, shouldJoinBefore);
  }

  //TODO: Here we can use mongo (4.0+) transaction management
  public void transfer(final String transferFrom, final ApiTransfer apiTransfer) {
    User transferTo = userRepository.findByUserName(apiTransfer.getTransferTo());
    if (transferTo == null) {
      throw new UserNotFoundException("UserName not found");
    }
    if (transferTo.getUserName().equals(transferFrom)) {
      throw new IllegalArgumentException("Self transfer is not allowed");
    }
    // subtract funds from transferFrom user
    User balanceAfterSubtract =
        userRepository.subtractBalance(transferFrom, apiTransfer.getAmount());
    if (balanceAfterSubtract == null) {
      throw new InsuffientFundsException("Insufficient balance");
    }
    // add funds to transferTo user
    userRepository.addBalance(transferTo.getUserName(), apiTransfer.getAmount());
  }

  private User toUser(final ApiCreateUser apiCreateUser) {
    return User.builder()
        .userName(apiCreateUser.getUserName())
        .password(passwordEncoder.encode(apiCreateUser.getPassword()))
        .fistName(apiCreateUser.getFirstName())
        .lastName(apiCreateUser.getLastName())
        .currentBalance(0L)
        .build();
  }

}
