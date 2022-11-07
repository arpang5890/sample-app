package com.sample.controller;

import com.sample.generated.api.v1.UserApi;
import com.sample.generated.model.v1.ApiCreateUser;
import com.sample.generated.model.v1.ApiTransfer;
import com.sample.generated.model.v1.ApiUser;
import com.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController implements UserApi {

  private final UserService userService;

  @Autowired
  public UserController(final UserService userService) {
    this.userService = userService;
  }

  @Override
  public ResponseEntity<Void> signUp(@Valid
  @RequestBody final ApiCreateUser apiCreateUser) {
    userService.createUser(apiCreateUser);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<ApiUser> joinFaucet() {
    userService.joinFaucet(SecurityContextHolder.getContext().getAuthentication().getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> transferTo(@Valid
  @RequestBody ApiTransfer apiTransfer) {
    userService.transfer(SecurityContextHolder.getContext().getAuthentication().getName(),
        apiTransfer);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
