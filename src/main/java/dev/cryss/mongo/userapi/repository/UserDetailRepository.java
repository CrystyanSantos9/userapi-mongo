package dev.cryss.mongo.userapi.repository;

import dev.cryss.mongo.userapi.domain.model.UserDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailRepository extends MongoRepository<UserDetailEntity, Long> {
    List<UserDetailEntity> findByFirstName(String firstName);
}
