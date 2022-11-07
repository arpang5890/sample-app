package com.sample.repository;

import com.sample.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class UserRepository {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public UserRepository(final MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void save(final User user) {
    mongoTemplate.save(user);
  }

  public User findByUserName(final String userName) {
    return mongoTemplate.findOne(new Query(
        Criteria.where("userName")
            .is(userName)), User.class);
  }

  public void reset(final LocalDateTime afterJoining) {
    Query query =
        new Query(
            Criteria.where("lastJoinedAt").lte(afterJoining));
    Update update =
        new Update().set("lastJoinedAt", null).set("currentBalance", 0);
    mongoTemplate.findAndModify(query, update,
        new FindAndModifyOptions().upsert(false),
        User.class);
  }


  public void addBalance(final String userName, Long balanceToAdd,
      LocalDateTime shouldJoinBefore) {
    Query query =
        new Query(
            Criteria.where("userName")
                .is(userName)
                .orOperator(Criteria.where("lastJoinedAt").isNull(),
                    Criteria.where("lastJoinedAt").lte(shouldJoinBefore)));
    Update update =
        new Update().set("lastJoinedAt", LocalDateTime.now()).inc("currentBalance", balanceToAdd);
    mongoTemplate.findAndModify(query, update,
        new FindAndModifyOptions().upsert(false),
        User.class);
  }

  public void addBalance(final String userName, Long balanceToAdd) {
    Query query =
        new Query(
            Criteria.where("userName").is(userName));
    Update update =
        new Update().inc("currentBalance", balanceToAdd);
    mongoTemplate.findAndModify(query, update,
        new FindAndModifyOptions().upsert(false),
        User.class);
  }

  public User subtractBalance(final String userName, Long balanceToSubtract) {
    Query query =
        new Query(
            Criteria.where("userName").is(userName).and("currentBalance").gte(balanceToSubtract));
    long subtract = (-balanceToSubtract);
    Update update =
        new Update().inc("currentBalance", subtract);
    return mongoTemplate.findAndModify(query, update,
        new FindAndModifyOptions().upsert(false),
        User.class);
  }

}
