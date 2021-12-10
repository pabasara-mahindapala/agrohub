package lk.agrohub.market.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lk.agrohub.market.model.SubCategory;
import lk.agrohub.market.model.Vehicle;
import lk.agrohub.market.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.agrohub.market.model.Journey;
import lk.agrohub.market.repository.JourneyRepository;

@Service
public class JourneyService {
    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

    public List<Journey> getAllJourneys(boolean isCompleted) {
        List<Journey> journeys = journeyRepository.findAll();
        journeys = journeys.stream().filter(journey -> journey.isCompleted() == isCompleted).collect(Collectors.toList());
        return journeys;
    }

    public List<Journey> getAllJourneysByTransporterId(long transporterId) {
        long vehicleId = vehicleRepository.findByTransporterId(transporterId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No vehicle for transporterId"))
                .getId();

        return journeyRepository.findByVehicleId(vehicleId);
    }

    public Journey journeySearchById(long _id) {
        return journeyRepository.findById(_id).orElse(null);
    }

    public Journey createJourney(Journey journey) {
        journey.setInsertDate(new Date());
        return this.journeyRepository.save(journey);
    }

    public Journey updateJourney(Journey journey) {
        journey.setLastUpdateDate(new Date());
        return this.journeyRepository.save(journey);
    }

    public void deleteJourney(Journey journey) {
        this.journeyRepository.delete(journey);
    }

}
