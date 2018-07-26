package com.spg.friendservice.api;

import com.alibaba.fastjson.JSON;
import com.spg.friendservice.BaseControllerTestSetup;
import com.spg.friendservice.dto.request.FriendConnectionRequest;
import com.spg.friendservice.dto.request.FriendListRequest;
import com.spg.friendservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FriendsControllerTest extends BaseControllerTestSetup  {

    public static final String JON_SNOW_GAME_COM = "jonSnow@game.com";
    public static final String YGITTE_GAME_COM = "ygritte@game.com";
    public static final String SAMWELL_GAME_COM = "samwell@game.com";

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
        userDao.save(User.builder()
                .email(YGITTE_GAME_COM)
                .friends(Collections.singletonList(JON_SNOW_GAME_COM))
                .build());


        FriendListRequest friendListRequest = FriendListRequest.builder()
                .email(YGITTE_GAME_COM)
                .build();

        mockMvc.perform(post("/api/friends/list")
                .content(JSON.toJSONString(friendListRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                .andExpect(jsonPath("$.friends[0]").value(JON_SNOW_GAME_COM))
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    void shouldReturnCommonFriendsBetweenTowEmail() throws Exception {
        userDao.save(User.builder()
                .email(YGITTE_GAME_COM)
                .friends(Collections.singletonList(JON_SNOW_GAME_COM))
                .build());

        userDao.save(User.builder()
                .email(SAMWELL_GAME_COM)
                .friends(Collections.singletonList(JON_SNOW_GAME_COM))
                .build());

        userDao.save(User.builder()
                .email(JON_SNOW_GAME_COM)
                .friends(Arrays.asList(SAMWELL_GAME_COM, YGITTE_GAME_COM))
                .build());

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
}