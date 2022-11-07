package com.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
  @Id
  private String id;
  @Indexed(unique = true)
  private String userName;
  private String password;
  private String fistName;
  private String lastName;
  private LocalDateTime lastJoinedAt;
  private Long currentBalance;
}
