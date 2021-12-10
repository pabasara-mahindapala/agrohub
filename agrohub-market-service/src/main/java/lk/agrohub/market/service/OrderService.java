package lk.agrohub.market.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lk.agrohub.market.dtos.CreateOrderInput;
import lk.agrohub.market.dtos.OrderDto;
import lk.agrohub.market.enums.OrderStatusEnum;
import lk.agrohub.market.model.*;
import lk.agrohub.market.repository.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    LocationRepository locationRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> orderSearchByProductId(long productId) {
        return orderRepository.findByProductId(productId);
    }

    public OrderDto ordersSearchById(long orderId) {
        Order order = orderRepository.findById(orderId).get();

        return new OrderDto(order, userRepository.findById(order.getProducerId()).get().getUsername(),
                productRepository.findById(order.getProductId()).get().getProductName(),
                userRepository.findById(order.getCustomerId()).get().getUsername(),
                productRepository.findById(order.getProductId()).get().getAvailableDate(),
                locationRepository.findById(order.getLocationId()).get().getLatitude(),
                locationRepository.findById(order.getLocationId()).get().getLongitude());
    }

    @Transactional
    public Order createOrder(CreateOrderInput createOrderInput) throws Exception {
        Order order = createOrderInput.getOrder();

        Product product = productRepository.findById(order.getProductId()).isPresent()
                ? productRepository.findById(order.getProductId()).get()
                : null;

        if (product != null) {
            if (product.getQuantity() < 1 || order.getQuantity() < 1) {
                throw new Exception("Unable to place order, incorrect quantity");
            }

            if (product.getQuantity() < order.getQuantity()) {
                throw new Exception("Unable to place order, insufficient quantity");
            }

            Location location = new Location(createOrderInput.getLongitude(), createOrderInput.getLatitude());

            long locationId = locationRepository.save(location).getId();

            Order newOrder = new Order(new Date(), new Date(), OrderStatusEnum.ADDED, order.getProductId(),
                    order.getProducerId(), order.getCustomerId(), locationId, order.getQuantity(),
                    order.getReceiverName(), order.getReceiverMobileNumber(), order.getDeliveryAddress(),
                    order.getDeliveryNearestCity(), order.getDeliveryPostalCode());

            Order returnOrder = this.orderRepository.save(newOrder);

            product.setQuantity(product.getQuantity() - newOrder.getQuantity());

            productRepository.save(product);

            return returnOrder;
        } else {
            throw new Exception("Unable to place order, invalid product");
        }
    }

    public void deleteOrder(Order order) {
        this.orderRepository.delete(order);
    }

    public List<OrderDto> getAllOrdersFiltered(Long productId, Long producerId, Long customerId,
                                               OrderStatusEnum status) {
        List<Order> orders = orderRepository.findByMultiple(productId, producerId, customerId, status);

        List<OrderDto> orderDtos = new ArrayList<OrderDto>();

        for (Order order : orders) {
            orderDtos.add(new OrderDto(order, userRepository.findById(order.getProducerId()).get().getUsername(),
                    productRepository.findById(order.getProductId()).get().getProductName(),
                    userRepository.findById(order.getCustomerId()).get().getUsername(),
                    productRepository.findById(order.getProductId()).get().getAvailableDate(),
                    locationRepository.findById(order.getLocationId()).get().getLatitude(),
                    locationRepository.findById(order.getLocationId()).get().getLongitude()));
        }

        return orderDtos;
    }

    public Order updateOrder(CreateOrderInput createOrderInput) {
        Order order = createOrderInput.getOrder();

        Optional<Location> optLocation = this.locationRepository.findById(order.getLocationId());
        if (optLocation.isPresent()) {
            Location location = optLocation.get();
            location.setLongitude(createOrderInput.getLongitude());
            location.setLatitude(createOrderInput.getLatitude());
            locationRepository.save(location);
        }

        order.setLastUpdateDate(new Date());
        return this.orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, OrderStatusEnum status) {
        Optional<Order> optOrder = this.orderRepository.findById(orderId);
        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            order.setStatus(status);
            order.setLastUpdateDate(new Date());
            return this.orderRepository.save(order);
        }
        return null;
    }
}
