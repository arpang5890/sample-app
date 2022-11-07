package com.sample.scheduler;

import com.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ResetBalanceScheduler {

  private final UserRepository userRepository;
  private final Long resetInMins;

  public ResetBalanceScheduler(final UserRepository userRepository,
      final @Value("${app.join.reset}") Long resetInMins) {
    this.userRepository = userRepository;
    this.resetInMins = resetInMins;
  }

  @Scheduled(initialDelay = 1000l, fixedDelay = 2000l)
  public void reset() {
    LocalDateTime resetAfter = LocalDateTime.now().minusMinutes(resetInMins);
    userRepository.reset(resetAfter);
  }
}
