package lk.agrohub.market.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lk.agrohub.market.model.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.agrohub.market.model.Vehicle;
import lk.agrohub.market.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    JourneyService journeyService;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        List<Long> busyVehicleIds = journeyService.getAllJourneys(false).stream()
                .map(Journey::getVehicleId)
                .collect(Collectors.toList());

        return vehicles.stream()
                .filter(vehicle -> !busyVehicleIds.contains(vehicle.getId()))
                .collect(Collectors.toList());
    }

    public Vehicle vehiclesSearchById(long _id) {
        return vehicleRepository.findById(_id).orElse(null);
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Vehicle vehicle) {
        vehicle.setLastUpdateDate(new Date());
        return this.vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Vehicle vehicle) {
        this.vehicleRepository.delete(vehicle);
    }
}
