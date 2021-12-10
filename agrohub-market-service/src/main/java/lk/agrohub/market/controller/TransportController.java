/**
 *
 */
package lk.agrohub.market.controller;

import lk.agrohub.market.dtos.CreateOrderInput;
import lk.agrohub.market.dtos.OrderDto;
import lk.agrohub.market.dtos.Route;
import lk.agrohub.market.enums.OrderStatusEnum;
import lk.agrohub.market.model.Order;
import lk.agrohub.market.service.OrderService;
import lk.agrohub.market.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/transport")
public class TransportController {
    Logger logger = LoggerFactory.getLogger(TransportController.class);

    @Autowired
    TransportService transportService;


    @GetMapping("/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> scheduleOrders() {
        try {
            this.transportService.scheduleOrders();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/test")
    public ResponseEntity<List<Route>> test(@RequestBody Object o) {
        try {
            logger.info(o.toString());

            List<Route> routeList = new ArrayList<>();
            List<Long> r1 = new ArrayList<>();
            r1.add(385L);

            routeList.add(new Route(r1));

            return ResponseEntity.ok().body(routeList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
