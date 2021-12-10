package lk.agrohub.market.controller;

import lk.agrohub.market.dtos.Counts;
import lk.agrohub.market.dtos.ProductDto;
import lk.agrohub.market.dtos.UserDto;
import lk.agrohub.market.model.*;
import lk.agrohub.market.repository.*;
import lk.agrohub.market.security.jwt.JwtUtils;
import lk.agrohub.market.security.request.LoginRequest;
import lk.agrohub.market.security.request.SignupRequest;
import lk.agrohub.market.security.request.UpdateRequest;
import lk.agrohub.market.security.response.JwtResponse;
import lk.agrohub.market.security.response.MessageResponse;
import lk.agrohub.market.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
        } catch (Exception e) {
            logger.error("Unable to authenticate", e);
            return new ResponseEntity<>(new MessageResponse("Unable to authenticate"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            // Create new user's account
            User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getFirstName(), signUpRequest.getLastName(), new Date(), new Date(),
                    signUpRequest.getBirthday(), signUpRequest.getMobileNumber(), signUpRequest.getNic(),
                    signUpRequest.getAddress(), signUpRequest.getNearestCity());

            Set<String> strRoles = signUpRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                throw new RuntimeException("Error: Role is not defined.");
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "buyer":
                            Role buyerRole = roleRepository.findByName(ERole.ROLE_BUYER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(buyerRole);

                            break;
                        case "seller":
                            Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(sellerRole);

                            break;
                        case "transporter":
                            Role transporterRole = roleRepository.findByName(ERole.ROLE_TRANSPORTER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(transporterRole);

                            break;
                        default:
                            throw new RuntimeException("Error: Role is not found.");
                    }
                });
            }

            user.setRoles(roles);
            user = userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully: " + user.getId()));
        } catch (Exception e) {
            logger.error("Unable to register", e);
            return new ResponseEntity<>(new MessageResponse("Unable to register"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> listAllUsers(String role) {
        try {
            List<User> users = userRepository.findAll();

            if (role != null) {
                users = users.stream().filter(user -> checkRole(user.getRoles(), role)).collect(Collectors.toList());
            }

            return new ResponseEntity<>(getUserDtos(users), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<UserDto> getUserDtos(List<User> users) {
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<ImageModel> images = imageRepository.findAll().stream().filter(imageModel -> userIds.contains(imageModel.getUserId())).collect(Collectors.toList());

        List<UserDto> userDtos = new ArrayList<>();
        for (User u : users) {
            Integer avgRating = getAvgRating(u.getReviews());
            userDtos.add(
                    new UserDto(
                            u,
                            avgRating,
                            getImageUrl(u.getId(), images)
                    ));
        }

        return userDtos;
    }

    private String getImageUrl(long userId, List<ImageModel> images) {
        Optional<ImageModel> image = images.stream().filter(imageModel -> imageModel.getUserId() == userId).findAny();
        return image.map(ImageModel::getUrl).orElse(null);
    }

    private boolean checkRole(Set<Role> roles, String role) {
        switch (role) {
            case "admin":
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                return roles.stream().allMatch(r -> Objects.equals(r.getId(), adminRole.getId()));
            case "buyer":
                Role buyerRole = roleRepository.findByName(ERole.ROLE_BUYER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                return roles.stream().allMatch(r -> Objects.equals(r.getId(), buyerRole.getId()));
            case "seller":
                Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                return roles.stream().allMatch(r -> Objects.equals(r.getId(), sellerRole.getId()));
            case "transporter":
                Role transporterRole = roleRepository.findByName(ERole.ROLE_TRANSPORTER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                return roles.stream().allMatch(r -> Objects.equals(r.getId(), transporterRole.getId()));
            default:
                return false;
        }
    }

    private Integer getAvgRating(Set<Review> reviews) {
        if (reviews.size() == 0) {
            return null;
        }

        int sum = 0;

        for (Review review : reviews) {
            sum += review.getRating();
        }

        return Math.round(sum / reviews.size());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);

            // throw exception if null
            if (!user.isPresent()) {
                throw new RuntimeException("User not found");
            }

            Integer avgRating = getAvgRating(user.get().getReviews());

            UserDto userDto = new UserDto(user.get(), avgRating,
                    imageRepository.findByUserId(userId) == null ? null : imageRepository.findByUserId(userId).getUrl());

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get user", e);
            return new ResponseEntity(new MessageResponse("Unable to get user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('ADMIN') or hasRole('TRANSPORTER')")
    public ResponseEntity<?> logoutUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest updateRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(updateRequest.getUsername());

        if (optionalUser.isPresent()) {
            User oldUser = optionalUser.get();

            if (updateRequest.getFirstName() != null) {
                oldUser.setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getLastName() != null) {
                oldUser.setLastName(updateRequest.getLastName());
            }
            if (updateRequest.getBirthday() != null) {
                oldUser.setBirthday(updateRequest.getBirthday());
            }
            if (updateRequest.getMobileNumber() != null) {
                oldUser.setMobileNumber(updateRequest.getMobileNumber());
            }
            if (updateRequest.getNic() != null) {
                oldUser.setNic(updateRequest.getNic());
            }
            if (updateRequest.getAddress() != null) {
                oldUser.setAddress(updateRequest.getAddress());
            }
            if (updateRequest.getNearestCity() != null) {
                oldUser.setNearestCity(updateRequest.getNearestCity());
            }

            oldUser.setLastUpdateDate(new Date());

            Set<String> strRoles = updateRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                throw new RuntimeException("Error: Role is not defined.");
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "buyer":
                            Role buyerRole = roleRepository.findByName(ERole.ROLE_BUYER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(buyerRole);

                            break;
                        case "seller":
                            Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(sellerRole);

                            break;
                        case "transporter":
                            Role transporterRole = roleRepository.findByName(ERole.ROLE_TRANSPORTER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(transporterRole);

                            break;
                        default:
                            throw new RuntimeException("Error: Role is not found.");
                    }
                });
            }

            oldUser.setRoles(roles);
            userRepository.save(oldUser);

            return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
        } else {
            return new ResponseEntity<>(new MessageResponse("User not present"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);

            // throw exception if null
            if (!user.isPresent()) {
                throw new RuntimeException("User not found");
            }

            userRepository.delete(user.get());

            return new ResponseEntity<>(new MessageResponse("Deleted user : " + user.get().getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete user", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/review/{userId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Review> reviewUser(@PathVariable long userId, @RequestBody Review review) throws Exception {
        ResponseEntity<Review> result;
        try {
            String currentUserName;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                currentUserName = authentication.getName();
                Optional<User> reviewer = userRepository.findByUsername(currentUserName);

                review.setReviewerId(reviewer.get().getId());
                review.setInsertDate(new Date());

                Review addedReview = reviewRepository.save(review);
                User reviewedUser = userRepository.findById(userId).get();
                Set<Review> reviews = reviewedUser.getReviews();

                if (reviews == null) {
                    reviews = new HashSet<>();
                }

                reviews.add(addedReview);
                reviewedUser.setReviews(reviews);
                userRepository.save(reviewedUser);
                result = new ResponseEntity<>(review, HttpStatus.OK);
            } else {
                result = new ResponseEntity<>(review, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            logger.error("Unable to review", e);
            result = new ResponseEntity<>(review, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/images/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<?> addImage(@RequestParam String imageName, @RequestParam String imageUrl, @RequestParam String imageType, @RequestParam Long userId) {
        try {
            ImageModel image = new ImageModel(1, userId, null, imageName, imageType, new Date(), imageUrl);
            this.imageRepository.save(image);
            return new ResponseEntity<>(new MessageResponse("Added image : " + imageName), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to add img", e);
            return new ResponseEntity<>(new MessageResponse("Unable to add img"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/counts")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Counts> getCounts() {
        try {
            List<User> users = userRepository.findAll();
            List<User> farmers = users.stream().filter(user -> checkRole(user.getRoles(), "seller")).collect(Collectors.toList());
            long farmersCount = farmers.size();
            long buyersCount = users.stream().filter(user -> checkRole(user.getRoles(), "buyer")).count();

            long productsCount = productRepository.count();
            long ordersCount = orderRepository.count();

            return new ResponseEntity<>(new Counts(
                    farmersCount,
                    buyersCount,
                    productsCount,
                    ordersCount
            ), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bestsellers")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getBestSellers() {
        try {
            List<User> users = userRepository.findAll();
            List<User> farmers = users.stream().filter(user -> checkRole(user.getRoles(), "seller")).collect(Collectors.toList());

            HashMap<User, Long> farmerOrderCounts = new HashMap<>();

            for (User farmer : farmers) {
                long orderCount = orderRepository.findByProducerId(farmer.getId()).size();
                farmerOrderCounts.put(farmer, orderCount);
            }

            farmerOrderCounts = reverseSortByValue(farmerOrderCounts);

            List<User> bestSellerUsers = farmerOrderCounts.keySet().stream().limit(5).collect(Collectors.toList());

            return new ResponseEntity<>(getUserDtos(bestSellerUsers), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get best sellers", e);
            return new ResponseEntity(new MessageResponse("Unable to get best sellers"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static HashMap<User, Long> reverseSortByValue(HashMap<User, Long> hm) {
        List<Map.Entry<User, Long>> list =
                new LinkedList<>(hm.entrySet());

        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        HashMap<User, Long> temp = new LinkedHashMap<>();

        for (Map.Entry<User, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }

}
