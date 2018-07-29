package com.spg.friendservice.service;

import com.spg.friendservice.dao.UserDao;
import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.dto.request.SubscriptionRequest;
import com.spg.friendservice.dto.request.UpdateMessageRequest;
import com.spg.friendservice.exception.*;
import com.spg.friendservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        final List<String> blacklistEmails = users.stream()
                .filter(user -> !CollectionUtils.isEmpty(user.getBlacklist()))
                .flatMap(user -> user.getBlacklist().stream())
                .collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(blacklistEmails)) {
            throw new FriendsConnectionException();
        }
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
        if (subscriptionRequest.getTarget().equals(subscriptionRequest.getRequestor())) {
            throw new SelfSubscriptionException();
        }

        if (CollectionUtils.isEmpty(requestor.getSubscription())) {
           requestor.setSubscription(Collections.singletonList(subscriptionRequest.getTarget()));
        } else if (!requestor.getSubscription().contains(target.getEmail())) {
            requestor.getSubscription().add(subscriptionRequest.getTarget());
        } else if (requestor.getSubscription().contains(target.getEmail())){
            throw new DuplicateSubscriptionException(requestor.getEmail(), target.getEmail());
        }
        userDao.save(requestor);
    }

    public void blacklist(SubscriptionRequest subscriptionRequest) {
        final User requestor = userDao.findByEmail(subscriptionRequest.getRequestor())
                .orElseThrow(RequestorNotExistException::new);
        final User target = userDao.findByEmail(subscriptionRequest.getTarget())
                .orElseThrow(TargetNotExistException::new);
        if (subscriptionRequest.getTarget().equals(subscriptionRequest.getRequestor())) {
            throw new SelfBlacklistException();
        }

        if (CollectionUtils.isEmpty(requestor.getBlacklist())) {
            requestor.setBlacklist(Collections.singletonList(subscriptionRequest.getTarget()));
        } else if (!requestor.getBlacklist().contains(target.getEmail())) {
            requestor.getBlacklist().add(subscriptionRequest.getTarget());
        } else if (requestor.getBlacklist().contains(target.getEmail())) {
            throw new DuplicateBlacklistException(requestor.getEmail(), target.getEmail());
        }
        if(!CollectionUtils.isEmpty(requestor.getSubscription())) {
            requestor.getSubscription().remove(subscriptionRequest.getTarget());
        }
        if(!CollectionUtils.isEmpty(requestor.getFriends())) {
            requestor.getFriends().remove(subscriptionRequest.getTarget());
        }
        if(!CollectionUtils.isEmpty(target.getFriends())) {
            target.getFriends().remove(subscriptionRequest.getRequestor());
        }
        userDao.save(requestor);
        userDao.save(target);
    }

    public List<String> getRecipients(UpdateMessageRequest updateMessageRequest) {
        List<User> recipients = new ArrayList<>();
        final List<User> friends = userDao.findAllByFriendsContaining(updateMessageRequest.getSender());
        final List<User> subscribers = userDao.findAllBySubscriptionContaining(updateMessageRequest.getSender());

        recipients.addAll(friends);
        recipients.addAll(subscribers);
        return recipients.stream().map(User::getEmail).collect(Collectors.toList());
    }
}
