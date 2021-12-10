package lk.agrohub.market.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.Location;

public interface LocationRepository extends MongoRepository<Location, Long> {

}
