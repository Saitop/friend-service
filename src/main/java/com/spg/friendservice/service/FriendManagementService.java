package com.spg.friendservice.service;

import com.spg.friendservice.dao.UserDao;
import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.exception.UserNotFountException;
import com.spg.friendservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class FriendManagementService {

    private final UserDao userDao;

    @Autowired
    public FriendManagementService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> creatConnectionByEmails(FriendConnectionRequest friendConnectionRequest) {
        final List<User> users = friendConnectionRequest.getFriends().stream().map(email -> {
            final Optional<User> userOptional = userDao.findByEmail(email);
            return userOptional.orElseGet(() -> User.builder().email(email).build());
        }).collect(Collectors.toList());
        users.forEach(user -> {
            final List<String> otherUserEmails = users.stream()
                    .filter(u -> !u.getEmail().equals(user.getEmail())
                            && !(nonNull(user.getFriends()) &&
                            user.getFriends().contains(u.getEmail()))
                    )
                    .map(User::getEmail)
                    .collect(Collectors.toList());
            user.getFriends().addAll(otherUserEmails);
        });
        return userDao.saveAll(users);
    }

    public List<String> getFriendsByEmail(FriendListRequest friendListRequest) {
        User user = userDao.findByEmail(friendListRequest.getEmail())
                .orElseThrow(UserNotFountException::new);
        return user.getFriends();
    }

    public List<String> getCommonFriends(FriendConnectionRequest friendConnectionRequest) {
        List<User> user = userDao.findAllByFriendsContains(friendConnectionRequest.getFriends());
        return user.stream().map(User::getEmail).collect(Collectors.toList());
    }
}
