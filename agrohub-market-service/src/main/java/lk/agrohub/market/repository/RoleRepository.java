package lk.agrohub.market.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.ERole;
import lk.agrohub.market.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
