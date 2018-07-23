package com.spg.friendservice.api;

import com.spg.friendservice.dto.FriendConnectionDto;
import com.spg.friendservice.dto.SuccessResponse;
import com.spg.friendservice.service.FriendManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public SuccessResponse createFriendsConnection(@Valid @RequestBody FriendConnectionDto friendConnectionDto) {
        friendManagementService.creatConnectionByEmails(friendConnectionDto);
        return SuccessResponse.builder().build();
    }
}
