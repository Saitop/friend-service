package com.spg.friendservice.api;

import com.alibaba.fastjson.JSON;
import com.spg.friendservice.BaseControllerTestSetup;
import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.dto.request.SubscriptionRequest;
import com.spg.friendservice.fixture.UserBuilder;
import com.spg.friendservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.spg.friendservice.fixture.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FriendsControllerTest extends BaseControllerTestSetup  {

    @Autowired
    private UserBuilder userBuilder;

    @Test
    void shouldReturnSuccessWhenCreateConnectionBetweenTowEmail() throws Exception {
        FriendConnectionRequest friendConnectionRequest = FriendConnectionRequest.builder()
                .friends(Arrays.asList(JON_SNOW_EMAIL, YGITTE_EMAIL))
                .build();

        mockMvc.perform(post("/api/friends")
                .content(JSON.toJSONString(friendConnectionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE));

        Optional<User> user = userDao.findByEmail(JON_SNOW_EMAIL);
        assertTrue(user.isPresent());
        assertEquals(JON_SNOW_EMAIL, user.get().getEmail());
        assertEquals(YGITTE_EMAIL, user.get().getFriends().get(0));
    }

    @Test
    void shouldReturnFriendsByEmail() throws Exception {
        final User ygritt = userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();

        FriendListRequest friendListRequest = FriendListRequest.builder()
                .email(YGITTE_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/list")
                .content(JSON.toJSONString(friendListRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                .andExpect(jsonPath("$.friends[0]").value(ygritt.getFriends().get(0)))
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    void shouldReturnCommonFriendsBetweenTowEmail() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();
        userBuilder.withDefault()
                .withEmail(SAMWELL_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();
        userBuilder.withDefault()
                .withEmail(JON_SNOW_EMAIL)
                .withFriends(Arrays.asList(SAMWELL_EMAIL, YGITTE_EMAIL))
                .persist();

        FriendConnectionRequest friendConnectionRequest = FriendConnectionRequest.builder()
                .friends(Arrays.asList(SAMWELL_EMAIL, YGITTE_EMAIL))
                .build();

        mockMvc.perform(post("/api/friends/common")
                .content(JSON.toJSONString(friendConnectionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                .andExpect(jsonPath("$.friends[0]").value(JON_SNOW_EMAIL))
                .andExpect(jsonPath("$.count").value(1));

    }

    @Test
    void shouldSubscribeSuccessfullyWhenRequestorExist() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();
        userBuilder.withDefault()
                .withEmail(JON_SNOW_EMAIL)
                .withFriends(Arrays.asList(SAMWELL_EMAIL, YGITTE_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(JON_SNOW_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE));

        Optional<User> ygritte = userDao.findByEmail(YGITTE_EMAIL);
        assertTrue(ygritte.isPresent());
        assertEquals(ygritte.get().getSubscription().size(), 1) ;
        assertEquals(ygritte.get().getSubscription().get(0), JON_SNOW_EMAIL) ;
    }

    @Test
    void shouldReturnTargetNotExistException() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(JON_SNOW_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Target is not exist."));
    }

    @Test
    void shouldReturnRequestorNotExistException() throws Exception {
        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(JON_SNOW_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Requestor is not exist."));
    }

    @Test
    void shouldReturnSubscriptionExceptionWhenDoingDuplicateSubscription() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .withSubscription(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();

        userBuilder.withDefault()
                .withEmail(JON_SNOW_EMAIL)
                .withFriends(Arrays.asList(SAMWELL_EMAIL, YGITTE_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(JON_SNOW_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Requestor(ygritte@game.com) had already subscribed to target (jonSnow@game.com).")
                );
    }

    @Test
    void shouldReturnSelfSubscriptionExceptionIfSubscribeToOneself() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(YGITTE_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Cannot subscribe to oneself."));
    }

    @Test
    void shouldBlacklist() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .withSubscription(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();
        userBuilder.withDefault()
                .withEmail(JON_SNOW_EMAIL)
                .withFriends(Arrays.asList(SAMWELL_EMAIL, YGITTE_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(JON_SNOW_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/blacklist")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE));

        Optional<User> ygritte = userDao.findByEmail(YGITTE_EMAIL);
        assertTrue(ygritte.isPresent());
        assertEquals(ygritte.get().getBlacklist().size(), 1) ;
        assertEquals(ygritte.get().getBlacklist().get(0), JON_SNOW_EMAIL) ;
        assertEquals(ygritte.get().getSubscription().size(), 0) ;
        assertEquals(ygritte.get().getFriends().size(), 0) ;
    }

    @Test
    void shouldReturnSelBlacklistExceptionIfBlacklistOneself() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withFriends(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(YGITTE_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/blacklist")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Cannot blacklist oneself."));
    }

    @Test
    void shouldReturnBlacklistExceptionWhenDoingDuplicateBlacklist() throws Exception {
        userBuilder.withDefault()
                .withEmail(YGITTE_EMAIL)
                .withBlacklist(Collections.singletonList(JON_SNOW_EMAIL))
                .persist();

        userBuilder.withDefault()
                .withEmail(JON_SNOW_EMAIL)
                .withFriends(Arrays.asList(SAMWELL_EMAIL, YGITTE_EMAIL))
                .persist();

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_EMAIL)
                .target(JON_SNOW_EMAIL)
                .build();

        mockMvc.perform(post("/api/friends/blacklist")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Requestor(ygritte@game.com) had already blacklisted target (jonSnow@game.com).")
                );
    }
}