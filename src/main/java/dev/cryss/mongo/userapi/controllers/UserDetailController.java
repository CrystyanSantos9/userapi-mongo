package dev.cryss.mongo.userapi.controllers;

import dev.cryss.mongo.userapi.controllers.exception.ResourceNotFoundException;
import dev.cryss.mongo.userapi.controllers.mapper.UserDetailMapper;
import dev.cryss.mongo.userapi.domain.model.UserDetailEntity;
import dev.cryss.mongo.userapi.domain.request.UserInputRequest;
import dev.cryss.mongo.userapi.domain.response.UserDataDetailResponse;
import dev.cryss.mongo.userapi.repository.UserDetailRepository;
import dev.cryss.mongo.userapi.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserInput;
import org.openapitools.model.UsersDataDetail;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserDetailController implements UsersApi {


    private final UserDetailMapper userDetailMapperObjectMapper;


    private final UserDetailRepository userDetailRepository;


    private final SequenceGeneratorService sequence;

    @Override
    public ResponseEntity<UsersDataDetail> getUsers(String firstName) {

        List<UserDetailEntity> entityList;

        if(StringUtils.isNotBlank (firstName)){
            entityList = userDetailRepository.findByFirstName (firstName);
        }else{
            entityList = userDetailRepository.findAll();
        }


      UserDataDetailResponse userDataDetailResponse = userDetailMapperObjectMapper.toUserDataDetailResponse (null, entityList);

      UsersDataDetail response = userDetailMapperObjectMapper.toUsersDataDetail (userDataDetailResponse);

      return ResponseEntity.ok ().body (response);
    }


    @Override
    public ResponseEntity<Void> saveUser(UserInput userInput) {
        // da camada de entrada para a service
        UserInputRequest userRequest = userDetailMapperObjectMapper.toUserInputRequest (userInput);

        // da service para a repository
        UserDetailEntity userEntity = userDetailMapperObjectMapper.toUserDetailEntity (userRequest);

        //Criar id
        //Dependendo da regra de negócio ia uma classe de validação aqui//na service
        //para lançar exception em caso de um dado nulo, faltante, ou que nao atendesse
        //a um criterio do regra de negócio
        userEntity.setId (sequence.generateSequence (UserDetailEntity.SEQUENCE_NAME));
       var save =  userDetailRepository.save (userEntity);

        System.out.println (save);

        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> updateUserById(Long userId, UserInput userInput) throws ResourceNotFoundException {

        UserDetailEntity userDetailEntity = userDetailRepository.findById (userId)
                .orElseThrow (()-> new ResourceNotFoundException ("User not found for this id:: " + userId));

        userDetailEntity.setFirstName (userInput.getFirstName ());
        userDetailEntity.setLastName (userInput.getLastName ());


        final UserDetailEntity userUpdate = userDetailRepository.save (userDetailEntity);


        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> removeUser(Long userId) throws ResourceNotFoundException {

        UserDetailEntity userDetailEntity = userDetailRepository.findById (userId)
                .orElseThrow (()->new ResourceNotFoundException("Employee not found for this id :: " + userId));

        userDetailRepository.delete (userDetailEntity);

        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }
}
