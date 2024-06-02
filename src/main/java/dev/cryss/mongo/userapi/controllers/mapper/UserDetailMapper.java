package dev.cryss.mongo.userapi.controllers.mapper;

import dev.cryss.mongo.userapi.domain.model.UserDetailEntity;
import dev.cryss.mongo.userapi.domain.request.UserInputRequest;
import dev.cryss.mongo.userapi.domain.response.UserDataDetailResponse;
import dev.cryss.mongo.userapi.domain.response.UserDetailResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.UserDetail;
import org.openapitools.model.UserInput;
import org.openapitools.model.UsersDataDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserDetailMapper {

    UsersDataDetail toUsersDataDetail(UserDataDetailResponse userDataDetailResponse);

    UserDetail toUserDetail(UserDetailResponse UserDetailResponse);


    UserDataDetailResponse toUserDataDetailResponse(UsersDataDetail usersDataDetail);


    UserDetailResponse toUserDetailResponse(UserDetail userDetail);

    UserDetailResponse toUserDetailResponse(UserDetailEntity userDetailEntity);


    UserInputRequest toUserInputRequest(UserInput userInput);

    UserDetailEntity toUserDetailEntity(UserInputRequest userInputRequest);

    @Mapping(target = "data", source = "data")
    UserDataDetailResponse toUserDataDetailResponse(Integer dummy, List<UserDetailEntity> data);

}
