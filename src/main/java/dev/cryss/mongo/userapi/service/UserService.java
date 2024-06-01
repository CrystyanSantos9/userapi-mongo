package dev.cryss.mongo.userapi.service;

import dev.cryss.mongo.userapi.controllers.exception.ResourceNotFoundException;
import dev.cryss.mongo.userapi.controllers.mapper.UserDetailMapper;
import dev.cryss.mongo.userapi.domain.model.UserDetailEntity;
import dev.cryss.mongo.userapi.domain.request.UserInputRequest;
import dev.cryss.mongo.userapi.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.UserInput;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailRepository userDetailRepository;
    private final UserDetailMapper userDetailMapperObjectMapper;

    private final SequenceGeneratorService sequence;


    public List<UserDetailEntity> findAllUsers(String firstName){

        List<UserDetailEntity> entityList;

        if(StringUtils.isNotBlank (firstName)){
            entityList = userDetailRepository.findByFirstName (firstName);
        }else{
            entityList = userDetailRepository.findAll();
        }

        return entityList;

    }

    public UserDetailEntity saveUser(UserInputRequest userInputRequest) {
        UserDetailEntity userEntity = userDetailMapperObjectMapper.toUserDetailEntity (userInputRequest);

        userEntity.setId (sequence.generateSequence (UserDetailEntity.SEQUENCE_NAME));
        return userDetailRepository.save (userEntity);

    }

    public UserDetailEntity updateUserById(Long userId, UserInputRequest userInputRequest) throws ResourceNotFoundException {

        UserDetailEntity userDetailEntity = userDetailRepository.findById (userId)
                .orElseThrow (() -> new ResourceNotFoundException ("User not found for this id:: " + userId));

        userDetailEntity.setFirstName (userInputRequest.getFirstName ());
        userDetailEntity.setLastName (userInputRequest.getLastName ());


        return userDetailRepository.save (userDetailEntity);
    }

    public void removeUser(Long userId) throws ResourceNotFoundException {
        UserDetailEntity userDetailEntity = userDetailRepository.findById (userId)
                .orElseThrow (()->new ResourceNotFoundException("Employee not found for this id :: " + userId));

        userDetailRepository.delete (userDetailEntity);
    }


}
