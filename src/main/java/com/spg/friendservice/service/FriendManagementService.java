package com.spg.friendservice.service;

import com.spg.friendservice.dao.UserDao;
import com.spg.friendservice.dto.FriendConnectionDto;
import com.spg.friendservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendManagementService {

    private final UserDao userDao;

    @Autowired
    public FriendManagementService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> creatConnectionByEmails(FriendConnectionDto friendConnectionDto) {
        final List<User> users = friendConnectionDto.getFriends().stream().map(email -> {
            final Optional<User> userOptional = userDao.findByEmail(email);
            return userOptional.orElseGet(() -> User.builder().email(email).build());
        }).collect(Collectors.toList());
        users.stream().forEach(user -> {
            final List<String> otherUserEmails = users.stream()
                    .filter(u -> !u.getEmail().equals(user.getEmail())
                            && !(Objects.nonNull(user.getFriends()) &&
                            user.getFriends().contains(u.getEmail()))
                    )
                    .map(User::getEmail)
                    .collect(Collectors.toList());
            user.getFriends().addAll(otherUserEmails);
        });
        final List<User> newUsers = userDao.saveAll(users);
        return newUsers;
    }
}
