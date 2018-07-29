package com.spg.friendservice.service;

import com.spg.friendservice.dao.UserDao;
import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.dto.request.SubscriptionRequest;
import com.spg.friendservice.exception.DuplicateSubscriptionException;
import com.spg.friendservice.exception.RequestorNotExistException;
import com.spg.friendservice.exception.TargetNotExistException;
import com.spg.friendservice.exception.UserNotFountException;
import com.spg.friendservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        final List<User> users = userDao.findAllByFriendsContains(friendConnectionRequest.getFriends());

        final Map<String, List<String>> FriendMap = users.stream()
                .collect(Collectors.toMap(User::getEmail, User::getFriends));

        final List<User> commonFriends = users.stream().filter(user
                -> FriendMap.get(user.getEmail()).containsAll(friendConnectionRequest.getFriends()))
                .collect(Collectors.toList());

        return commonFriends.stream().map(User::getEmail).collect(Collectors.toList());
    }

    public void createSubscription(SubscriptionRequest subscriptionRequest) {
        final User requestor = userDao.findByEmail(subscriptionRequest.getRequestor())
                .orElseThrow(RequestorNotExistException::new);
        final User target = userDao.findByEmail(subscriptionRequest.getTarget())
                .orElseThrow(TargetNotExistException::new);
        if (!requestor.getSubscription().contains(target.getEmail())) {
            requestor.getSubscription().add(subscriptionRequest.getTarget());
            userDao.save(requestor);
        } else {
            throw new DuplicateSubscriptionException(requestor.getEmail(), target.getEmail());
        }
    }
}
