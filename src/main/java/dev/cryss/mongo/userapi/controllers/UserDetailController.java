package dev.cryss.mongo.userapi.controllers;

import dev.cryss.mongo.userapi.controllers.exception.ResourceNotFoundException;
import dev.cryss.mongo.userapi.controllers.mapper.UserDetailMapper;
import dev.cryss.mongo.userapi.domain.request.UserInputRequest;
import dev.cryss.mongo.userapi.domain.response.UserDataDetailResponse;
import dev.cryss.mongo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserInput;
import org.openapitools.model.UsersDataDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserDetailController implements UsersApi {


    private final UserDetailMapper userDetailMapperObjectMapper;

    private final UserService userService;



    @Override
    public ResponseEntity<UsersDataDetail> getUsers(String firstName) {


      UserDataDetailResponse userDataDetailResponse = userDetailMapperObjectMapper.
              toUserDataDetailResponse (null, userService.findAllUsers (firstName));

      UsersDataDetail response = userDetailMapperObjectMapper.toUsersDataDetail (userDataDetailResponse);

      return ResponseEntity.ok ().body (response);

    }


    @Override
    public ResponseEntity<Void> saveUser(UserInput userInput) {
        // da camada de entrada para a service
        UserInputRequest userRequest = userDetailMapperObjectMapper.toUserInputRequest (userInput);

        System.out.println (userService.saveUser (userRequest));

        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> updateUserById(Long userId, UserInput userInput)  {

        UserInputRequest userInputRequest = userDetailMapperObjectMapper.toUserInputRequest (userInput);

        try {
            userService.updateUserById (userId,userInputRequest);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException (e);
        }

        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> removeUser(Long userId) throws ResourceNotFoundException {

        userService.removeUser (userId);

        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }
}
