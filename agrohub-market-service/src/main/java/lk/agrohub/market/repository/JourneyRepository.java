package lk.agrohub.market.repository;

import lk.agrohub.market.model.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.Journey;

import java.util.List;

public interface JourneyRepository extends MongoRepository<Journey, Long> {
    List<Journey> findByVehicleId(long vehicleId);
}
