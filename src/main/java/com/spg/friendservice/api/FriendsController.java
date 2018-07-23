package com.spg.friendservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/friends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FriendsController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> findAllFriends() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("key", "friends");
        return map;
    }
}
