package com.spg.friendservice.api;

import com.spg.friendservice.dto.FriendConnectionDto;
import com.spg.friendservice.service.FriendManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/friends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FriendsController {

    @Autowired
    private FriendManagementService friendManagementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FriendConnectionDto createFriendsConnection(@Valid @RequestBody FriendConnectionDto friendConnectionDto) {
        friendManagementService.creatConnectionByEmails(friendConnectionDto);
        return friendConnectionDto;
    }
}
