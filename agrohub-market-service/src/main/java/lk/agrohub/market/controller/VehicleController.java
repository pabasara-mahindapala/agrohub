package lk.agrohub.market.controller;

import java.util.ArrayList;
import java.util.List;

import lk.agrohub.market.model.Node;
import lk.agrohub.market.security.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lk.agrohub.market.model.Vehicle;
import lk.agrohub.market.service.VehicleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/vehicle")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<Vehicle>> listAllVehicle() {
        try {
            return new ResponseEntity<>(this.vehicleService.getAllVehicles(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get list", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{vehicleId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable long vehicleId) {
        try {
            Vehicle vehicle = vehicleService.vehiclesSearchById(vehicleId);

            // throw exception if null
            if (vehicle == null) {
                throw new RuntimeException("Vehicle not found");
            }

            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get vehicle", e);
            return new ResponseEntity(new MessageResponse("Unable to get vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) throws Exception {
        ResponseEntity<Vehicle> result;
        try {
            vehicle = this.vehicleService.addVehicle(vehicle);
            result = new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to add vehicle", e);
            result = new ResponseEntity(new MessageResponse("Unable to add vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle) throws Exception {
        ResponseEntity<Vehicle> result;
        try {
            vehicle = this.vehicleService.updateVehicle(vehicle);
            result = new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update vehicle", e);
            result = new ResponseEntity(new MessageResponse("Unable to update vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{vehicleId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteVehicle(@PathVariable long vehicleId) {
        try {
            Vehicle vehicle = vehicleService.vehiclesSearchById(vehicleId);

            // throw exception if null

            if (vehicle == null) {
                throw new RuntimeException("Vehicle not found");
            }

            vehicleService.deleteVehicle(vehicle);

            return new ResponseEntity<>(new MessageResponse("Deleted Vehicle : " + vehicle.getRegNo()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete vehicle", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
