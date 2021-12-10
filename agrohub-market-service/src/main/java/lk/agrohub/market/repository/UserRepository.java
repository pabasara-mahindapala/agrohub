package lk.agrohub.market.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.User;


public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUsername(String username);

//	User findBy_id(ObjectId _id);

    Boolean existsByUsername(String username);
}