//package it.krisopea.springcors.route;
//
//import it.krisopea.springcors.repository.UserRepository;
//import it.krisopea.springcors.repository.VerificationRepository;
//import it.krisopea.springcors.repository.model.UserEntity;
//import it.krisopea.springcors.repository.model.VerificationEntity;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import org.apache.camel.builder.RouteBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class HouseKeepingRoute extends RouteBuilder {
//
//  @Value("${house.keeping.offset.minutes}")
//  Long houseKeepingOffsetMinutes;
//
//  @Autowired private UserRepository userRepository;
//  @Autowired private VerificationRepository verificationRepository;
//
//  @Override
//  public void configure() {
//    from("timer:houseKeepingTimer")
//        .routeId("houseKeepingTimer")
//        .delayer(60000)
//        .process(
//            exchange -> {
//              Instant lastUsefulDate = Instant.now().minus(houseKeepingOffsetMinutes, ChronoUnit.MINUTES);
//              List<VerificationEntity> verificationList = verificationRepository.findByCreateOn(lastUsefulDate);
//
//              if (!verificationList.isEmpty()) {
//                for (VerificationEntity verification : verificationList) {
//                  UserEntity user =
//                      userRepository
//                          .findByUsername(verification.getUserEntity().getUsername())
//                          .orElseThrow();
//                  verificationRepository.delete(verification);
//                  userRepository.delete(user);
//                  log.info("HouseKeeping: [{} account was deleted]", user.getUsername());
//                }
//              }
//            })
//        .log("Housekeeping process completed successfully");
//  }
//}
