package com.spg.friendservice.api;

import com.alibaba.fastjson.JSON;
import com.spg.friendservice.BaseControllerTestSetup;
import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.dto.request.SubscriptionRequest;
import com.spg.friendservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FriendsControllerTest extends BaseControllerTestSetup  {

    private static final String JON_SNOW_GAME_COM = "jonSnow@game.com";
    private static final String YGITTE_GAME_COM = "ygritte@game.com";
    private static final String SAMWELL_GAME_COM = "samwell@game.com";

    @Test
    void shouldReturnSuccessWhenCreateConnectionBetweenTowEmail() throws Exception {
        FriendConnectionRequest friendConnectionRequest = FriendConnectionRequest.builder()
                .friends(Arrays.asList(JON_SNOW_GAME_COM, YGITTE_GAME_COM))
                .build();

        mockMvc.perform(post("/api/friends")
                .content(JSON.toJSONString(friendConnectionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE));

        Optional<User> user = userDao.findByEmail(JON_SNOW_GAME_COM);
        assertTrue(user.isPresent());
        assertEquals(JON_SNOW_GAME_COM, user.get().getEmail());
        assertEquals(YGITTE_GAME_COM, user.get().getFriends().get(0));
    }

    @Test
    void shouldReturnFriendsByEmail() throws Exception {
        final User ygritt = saveUser(YGITTE_GAME_COM, Collections.singletonList(JON_SNOW_GAME_COM));

        FriendListRequest friendListRequest = FriendListRequest.builder()
                .email(YGITTE_GAME_COM)
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
        saveUser(YGITTE_GAME_COM, Collections.singletonList(JON_SNOW_GAME_COM));
        saveUser(SAMWELL_GAME_COM, Collections.singletonList(JON_SNOW_GAME_COM));
        saveUser(JON_SNOW_GAME_COM, Arrays.asList(SAMWELL_GAME_COM, YGITTE_GAME_COM));

        FriendConnectionRequest friendConnectionRequest = FriendConnectionRequest.builder()
                .friends(Arrays.asList(SAMWELL_GAME_COM, YGITTE_GAME_COM))
                .build();

        mockMvc.perform(post("/api/friends/common")
                .content(JSON.toJSONString(friendConnectionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                .andExpect(jsonPath("$.friends[0]").value(JON_SNOW_GAME_COM))
                .andExpect(jsonPath("$.count").value(1));

    }

    @Test
    void shouldSubscribeSuccessfullyWhenRequestorExist() throws Exception {
        saveUser(YGITTE_GAME_COM, Collections.singletonList(JON_SNOW_GAME_COM));
        saveUser(JON_SNOW_GAME_COM, Arrays.asList(SAMWELL_GAME_COM, YGITTE_GAME_COM));

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_GAME_COM)
                .target(JON_SNOW_GAME_COM)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE));

        Optional<User> ygritte = userDao.findByEmail(YGITTE_GAME_COM);
        assertTrue(ygritte.isPresent());
        assertEquals(ygritte.get().getSubscription().size(), 1) ;
        assertEquals(ygritte.get().getSubscription().get(0), JON_SNOW_GAME_COM) ;
    }

    @Test
    void shouldReturnRequestorNotExistException() throws Exception {
        saveUser(YGITTE_GAME_COM, Collections.singletonList(JON_SNOW_GAME_COM));
        saveUser(JON_SNOW_GAME_COM, Arrays.asList(SAMWELL_GAME_COM, YGITTE_GAME_COM));
        saveUser(YGITTE_GAME_COM, Collections.singletonList(JON_SNOW_GAME_COM));

        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_GAME_COM)
                .target(JON_SNOW_GAME_COM)
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
    void shouldReturnTargetNotExistException() throws Exception {
        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .requestor(YGITTE_GAME_COM)
                .target(JON_SNOW_GAME_COM)
                .build();

        mockMvc.perform(post("/api/friends/subscription")
                .content(JSON.toJSONString(subscriptionRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Requestor is not exist."));
    }
    
    private User saveUser(String email, List<String> friends) {
        return userDao.save(User.builder()
                .email(email)
                .friends(friends)
                .build());
    }
}