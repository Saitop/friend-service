package com.spg.friendservice.dao;

import com.spg.friendservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findAllByFriendsContains(List<String> emails);

    List<User> findAllByFriendsContaining(String email);

    List<User> findAllBySubscriptionContaining(String email);

    List<User> findAllByBlacklistContains(List<String> emails);
}
