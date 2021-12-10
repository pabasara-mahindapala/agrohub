package lk.agrohub.market.controller;

import java.util.ArrayList;
import java.util.List;

import lk.agrohub.market.model.Category;
import lk.agrohub.market.security.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lk.agrohub.market.model.Journey;
import lk.agrohub.market.service.JourneyService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/journey")
public class JourneyController {
    private static final Logger logger = LoggerFactory.getLogger(JourneyController.class);

    @Autowired
    JourneyService journeyService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<Journey>> listAllJourney(@RequestParam(required = false) Long transporterId) {
        try {
            if (transporterId != null) {
                return new ResponseEntity<>(journeyService.getAllJourneysByTransporterId(transporterId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(journeyService.getAllJourneys(), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Unable to get list", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{journeyId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Journey> getJourney(@PathVariable long journeyId) {
        try {
            Journey journey = journeyService.journeySearchById(journeyId);

            // throw exception if null

            if (journey == null) {
                throw new RuntimeException("Journey not found");
            }

            return new ResponseEntity<>(journey, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get journey", e);
            return new ResponseEntity(new MessageResponse("Unable to get journey"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Journey> createJourney(@RequestBody Journey journey) throws Exception {
        ResponseEntity<Journey> result;
        try {
            journey = this.journeyService.createJourney(journey);
            result = new ResponseEntity<>(journey, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to add journey", e);
            result = new ResponseEntity(new MessageResponse("Unable to add journey"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Journey> updateJourney(@RequestBody Journey journey) throws Exception {
        ResponseEntity<Journey> result;
        try {
            journey = this.journeyService.updateJourney(journey);
            result = new ResponseEntity<>(journey, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update journey", e);
            result = new ResponseEntity(new MessageResponse("Unable to update journey"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{journeyId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteJourney(@PathVariable long journeyId) {
        try {
            Journey journey = journeyService.journeySearchById(journeyId);

            // throw exception if null

            if (journey == null) {
                throw new RuntimeException("Journey not found");
            }

            journeyService.deleteJourney(journey);

            return new ResponseEntity<>(new MessageResponse("Deleted Journey"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete journey", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete journey"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
