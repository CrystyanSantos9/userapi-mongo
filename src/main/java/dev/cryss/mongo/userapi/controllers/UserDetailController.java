package dev.cryss.mongo.userapi.controllers;

import org.openapitools.api.UsersApi;
import org.openapitools.model.UserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDetailController implements UsersApi {
    @Override
    public ResponseEntity<List<UserDetail>> getUsers(String firstName) {
        return UsersApi.super.getUsers (firstName);
    }
}
