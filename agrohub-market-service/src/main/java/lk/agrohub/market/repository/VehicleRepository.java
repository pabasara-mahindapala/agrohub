package lk.agrohub.market.repository;

import lk.agrohub.market.model.Journey;
import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.Vehicle;

import java.util.List;

public interface VehicleRepository extends MongoRepository<Vehicle, Long> {
    List<Vehicle> findByTransporterId(long transporterId);
}
