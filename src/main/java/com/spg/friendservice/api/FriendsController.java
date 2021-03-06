package com.spg.friendservice.api;

import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.dto.request.SubscriptionRequest;
import com.spg.friendservice.dto.request.UpdateMessageRequest;
import com.spg.friendservice.dto.response.FriendListResponse;
import com.spg.friendservice.dto.response.RecipientListResponse;
import com.spg.friendservice.dto.response.SuccessResponse;
import com.spg.friendservice.service.FriendManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/friends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FriendsController {

    private final FriendManagementService friendManagementService;

    @Autowired
    public FriendsController(FriendManagementService friendManagementService) {
        this.friendManagementService = friendManagementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createFriendsConnection(@Valid @RequestBody FriendConnectionRequest friendConnectionRequest) {
        friendManagementService.creatConnectionByEmails(friendConnectionRequest);
        return SuccessResponse.builder().build();
    }

    /**
     * for a restful style api, should use http verb 'get' to query resource, like /api/friends?email=xxx@xx.com
     * but the requirement is to accept a json, so I must use 'post' to accept query body.
     *
    **/
    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public FriendListResponse getFriendsList(@Valid @RequestBody FriendListRequest FriendListRequest) {
        final List<String> friends = friendManagementService.getFriendsByEmail(FriendListRequest);
        return FriendListResponse.builder().friends(friends).count(friends.size()).build();
    }

    @PostMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public FriendListResponse getCommonFriends(@Valid @RequestBody FriendConnectionRequest friendConnectionRequest) {
        List<String> friends = friendManagementService.getCommonFriends(friendConnectionRequest);
        return FriendListResponse.builder().friends(friends).count(friends.size()).build();
    }

    @PostMapping("/subscription")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse subscribe(@Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        friendManagementService.createSubscription(subscriptionRequest);
        return SuccessResponse.builder().build();
    }

    @PostMapping("/blacklist")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse blacklist(@Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        friendManagementService.blacklist(subscriptionRequest);
        return SuccessResponse.builder().build();
    }

    @PostMapping("/updates")
    @ResponseStatus(HttpStatus.OK)
    public RecipientListResponse updates(@Valid @RequestBody UpdateMessageRequest updateMessageRequest) {
        final List<String> recipients = friendManagementService.getRecipients(updateMessageRequest);
        return RecipientListResponse.builder().recipients(recipients).count(recipients.size()).build();
    }
}
