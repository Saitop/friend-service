package com.spg.friendservice.fixture;

import com.spg.friendservice.dao.UserDao;
import com.spg.friendservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserBuilder {

    private User user;

    @Autowired
    private UserDao userDao;

    public UserBuilder withDefault() {
        user = User.builder()
                .email("")
                .friends(Collections.emptyList())
                .subscription(Collections.emptyList())
                .build();
        return this;
    }

    public UserBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withFriends(List<String> friends) {
        user.setFriends(friends);
        return this;
    }

    public UserBuilder withSubscription(List<String> subscription) {
        user.setSubscription(subscription);
        return this;
    }

    public User persist() {
        return this.userDao.save(user);
    }
}
