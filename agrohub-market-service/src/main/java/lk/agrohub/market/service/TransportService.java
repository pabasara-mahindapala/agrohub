package lk.agrohub.market.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.agrohub.market.dtos.OrderDto;
import lk.agrohub.market.dtos.Route;
import lk.agrohub.market.dtos.TransportPoint;
import lk.agrohub.market.dtos.TransportServiceInputDto;
import lk.agrohub.market.enums.OrderStatusEnum;
import lk.agrohub.market.model.Journey;
import lk.agrohub.market.model.Node;
import lk.agrohub.market.model.Order;
import lk.agrohub.market.model.Vehicle;
import lk.agrohub.market.repository.JourneyRepository;
import lk.agrohub.market.repository.NodeRepository;
import lk.agrohub.market.repository.OrderRepository;
import lk.agrohub.market.util.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportService {
    //    private static final String API_URL = "http://127.0.0.1:5000/generate-routes/";
    private static final String API_URL = "http://agrohub-travel-service.herokuapp.com/generate-routes/";
    Logger logger = LoggerFactory.getLogger(TransportService.class);

    @Value("${transport.service.secret}")
    private String transportServiceSecret;

    @Value("${transport.depot.latitude}")
    private String depotLatitude;

    @Value("${transport.depot.longitude}")
    private String depotLongitude;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    RestTemplate restTemplate;

    @Scheduled(cron = "0 0 1 * * MON")
    public void scheduleOrders() {
        logger.info("Starting to schedule");

        try {
            List<OrderDto> paidOrders = orderService.getAllOrdersFiltered(null, null, null, OrderStatusEnum.PAID);

            List<OrderDto> selectedOrders = paidOrders.stream().filter(orderDto -> {
                Date start = ConverterUtil.convertToDateViaInstant(LocalDate.now().minusDays(1));
                Date end = ConverterUtil.convertToDateViaInstant(LocalDate.now().plusDays(7));
                Date availableDate = orderDto.getAvailableDate();
                return availableDate.after(start) && availableDate.before(end);
            }).collect(Collectors.toList());

            List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();

            if (selectedOrders.isEmpty()) {
                logger.info("No paid unassigned orders found");
                return;
            }

            if (availableVehicles.isEmpty()) {
                logger.info("No vehicles available");
                return;
            }

            logger.info(String.format("Found %d paid unassigned orders within range", selectedOrders.size()));
            logger.info(String.format("%d vehicles available", availableVehicles.size()));

            List<Route> routes = getRoutesFromTransportService(availableVehicles.get(0).getCapacity(), selectedOrders);

            createJourneys(availableVehicles, routes, selectedOrders);
        } catch (Exception e) {
            logger.error("Unable to schedule", e);
        }
        logger.info("Finished scheduling");
    }

    private void createJourneys(List<Vehicle> availableVehicles, List<Route> routes, List<OrderDto> selectedOrders) {
        List<Order> scheduledOrders = new ArrayList<>();
        int iterations;

        if (availableVehicles.size() < routes.size()) {
            iterations = availableVehicles.size();
            logger.info(String.format("Vehicles are not enough. Need %d vehicles more", routes.size() - iterations));
        } else {
            iterations = routes.size();
        }

        for (int i = 0; i < iterations; i++) {
            List<Long> orderIdsRouted = routes.get(i).getOrderIdsRouted();

            List<OrderDto> routeOrderDtos = orderIdsRouted.stream().map(orderId -> getOrderDto(orderId, selectedOrders))
                    .collect(Collectors.toList());

            if (routeOrderDtos.isEmpty()) {
                logger.info("No orders available for route");
                continue;
            }

            Journey journey = new Journey(
                    routeOrderDtos.get(0).getOrder().getDeliveryNearestCity(),
                    routeOrderDtos.get(routeOrderDtos.size() - 1).getOrder().getDeliveryNearestCity(),
                    new Date(),
                    null,
                    new Date(),
                    false,
                    availableVehicles.get(i).getId()
            );

            journey = journeyRepository.save(journey);

            for (OrderDto orderDto : routeOrderDtos) {
                nodeRepository.save(new Node(
                        journey.getId(),
                        orderDto.getOrder().getLocationId(),
                        orderDto.getOrder().getId())
                );

                Order order = orderDto.getOrder();
                order.setStatus(OrderStatusEnum.SCHEDULED);
                scheduledOrders.add(order);
            }

            logger.info(String.format("Created journey with %d nodes", routeOrderDtos.size()));
        }

        orderRepository.saveAll(scheduledOrders);
        logger.info(String.format("Scheduled %d orders", scheduledOrders.size()));
    }

    private OrderDto getOrderDto(Long orderId, List<OrderDto> selectedOrders) {
        return selectedOrders.stream()
                .filter(selectedOrder -> selectedOrder.getOrder().getId() == orderId)
                .findAny()
                .orElse(null);
    }

    private List<Route> getRoutesFromTransportService(int vehicleCapacity, List<OrderDto> selectedOrders) throws JsonProcessingException {


        TransportServiceInputDto input = new TransportServiceInputDto(
                vehicleCapacity,
                transportServiceSecret,
                depotLatitude,
                depotLongitude);

        for (OrderDto orderDto : selectedOrders) {
            input.addPoint(new TransportPoint(
                    orderDto.getOrder().getId(),
                    orderDto.getLatitude(),
                    orderDto.getLongitude(),
                    orderDto.getOrder().getQuantity())
            );
        }

        logger.info(String.format("Selected %d points", input.getPoints().size()));

        return createRoutes(input);
    }

    private List<Route> createRoutes(TransportServiceInputDto input) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String inputJSONString = objectMapper.writeValueAsString(input);

        HttpEntity<String> request = new HttpEntity<>(inputJSONString);

        Route[] response = restTemplate.postForObject(API_URL, request, Route[].class);

        return response == null ? new ArrayList<>() : Arrays.asList(response);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
