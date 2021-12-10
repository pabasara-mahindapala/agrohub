/**
 *
 */
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

import lk.agrohub.market.dtos.CreateOrderInput;
import lk.agrohub.market.dtos.OrderDto;
import lk.agrohub.market.enums.OrderStatusEnum;
import lk.agrohub.market.model.Order;
import lk.agrohub.market.service.OrderService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    OrderService orderService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> listAllOrder(@RequestParam(required = false) Long productId,
                                                       @RequestParam(required = false) Long producerId, @RequestParam(required = false) Long customerId,
                                                       @RequestParam(required = false) OrderStatusEnum status) {
        try {
            return new ResponseEntity<>(this.orderService.getAllOrdersFiltered(productId, producerId, customerId, status), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get list", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long orderId) {

        try {
            OrderDto order = orderService.ordersSearchById(orderId);

            // throw exception if null
            if (order == null) {
                throw new RuntimeException("Order not found");
            }

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get order", e);
            return new ResponseEntity(new MessageResponse("Unable to get order"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderInput createOrderInput) throws Exception {
        ResponseEntity<Order> result;
        Order order = null;
        try {
            order = this.orderService.createOrder(createOrderInput);
            result = new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            result = new ResponseEntity<>(order, HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Unable to add order", e);
            result = new ResponseEntity(new MessageResponse("Unable to add order"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrder(@RequestBody CreateOrderInput createOrderInput) throws Exception {
        ResponseEntity<Order> result;
        Order order = null;
        try {
            order = this.orderService.updateOrder(createOrderInput);
            result = new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update order", e);
            result = new ResponseEntity(new MessageResponse("Unable to update order"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/updatestatus")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatusEnum status)
            throws Exception {
        ResponseEntity<Order> result;
        Order order = null;
        try {
            order = this.orderService.updateOrderStatus(orderId, status);
            result = new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update order status", e);
            result = new ResponseEntity(new MessageResponse("Unable to update order status"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable long orderId) {
        try {
            OrderDto order = orderService.ordersSearchById(orderId);

            // throw exception if null
            if (order == null) {
                throw new RuntimeException("Order not found");
            }

            orderService.deleteOrder(order.getOrder());

            return new ResponseEntity<>(new MessageResponse("Deleted Order : " + order.getOrder().getId()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete order", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete order"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
