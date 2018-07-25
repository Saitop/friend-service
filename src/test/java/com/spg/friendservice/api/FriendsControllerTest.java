package com.spg.friendservice.api;

import com.alibaba.fastjson.JSON;
import com.spg.friendservice.BaseControllerTestSetup;
import com.spg.friendservice.dto.FriendConnectionDto;
import com.spg.friendservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FriendsControllerTest extends BaseControllerTestSetup  {

    @Test
    void shouldReturnSuccessWhenCreateConnectionBetweenTowEmail() throws Exception {
        FriendConnectionDto friendConnectionDto = FriendConnectionDto.builder()
                .friends(Arrays.asList("jonSnow@game.com", "yigritte@game.com"))
                .build();

        mockMvc.perform(post("/api/friends")
                .content(JSON.toJSONString(friendConnectionDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE));

        Optional<User> user = userDao.findByEmail("jonSnow@game.com");
        assertTrue(user.isPresent());
        assertEquals("jonSnow@game.com", user.get().getEmail());
        assertEquals("yigritte@game.com", user.get().getFriends().get(0));
    }
}