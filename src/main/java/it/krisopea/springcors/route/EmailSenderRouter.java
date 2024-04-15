package it.krisopea.springcors.route;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.util.GlobalEmailResources;
import it.krisopea.springcors.util.constant.EmailEnum;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderRouter extends RouteBuilder {
  @Autowired private GlobalEmailResources globalEmailResources;
  @Autowired private UserRepository userRepository;
  @Autowired private VerificationRepository verificationRepository;

  @Override
  public void configure() {
    onException(Exception.class)
        .handled(true)
        .log(LoggingLevel.ERROR, "Error while sending the email: ${exception.message}");

    from("direct:sendEmail")
        .routeId("sendEmailRoute")
        .choice()
        .when(header("topic").isEqualTo(EmailEnum.REGISTRATION))
        .to("direct:sendRegistrationEmail")
        .when(header("topic").isEqualTo(EmailEnum.UPDATE))
        .to("direct:sendUpdateEmail")
        .when(header("topic").isEqualTo(EmailEnum.DELETE))
        .to("direct:sendDeleteEmail")
        .when(header("topic").isEqualTo(EmailEnum.LOGIN))
        .to("direct:sendLoginEmail")
        .when(header("topic").isEqualTo(EmailEnum.VERIFY))
        .to("direct:sendVerificationEmail")
        .end();

//    from("direct:sendRegistrationEmail")
//        .routeId("sendRegistrationEmailRoute")
//        .setHeader("subject", constant("Welcome!"))
//        .setBody(simple("Hi, thank you for registering to our service!"))
//        .process(
//            exchange -> {
//              globalEmailResources.incrementRegistrationEmailCounter();
//              AtomicInteger currentCount = globalEmailResources.getRegistrationEmailCounter();
//              exchange.setProperty("registrationEmailCount", currentCount);
//            });
//
//    from("direct:sendUpdateEmail")
//        .routeId("sendUpdateEmailRoute")
//        .routePolicy(routePolicy)
//        .setHeader("subject", constant("Update Notification"))
//        .setBody(
//            simple(
//                "We would like to notify you about the update of one or more fields of your"
//                    + " account."))
//        .process(exchange -> globalEmailResources.incrementUpdateEmailCounter());
//
//    from("direct:sendDeleteEmail")
//        .routeId("sendDeleteEmailRoute")
//        .routePolicy(routePolicy)
//        .setHeader("subject", constant("Cancellation Notification"))
//        .choice()
//        .when(header("isAdmin").isEqualTo(Boolean.TRUE.toString()))
//        .setBody(
//            simple(
//                "We want to notify you of the cancellation of your account by an administrator."))
//        .otherwise()
//        .setBody(
//            simple(
//                "We want to notify you of the cancellation of your account. We hope to see you"
//                    + " again soon!"))
//        .end()
//        .process(exchange -> globalEmailResources.incrementDeleteEmailCounter())
//        .to("direct:send");
//
//    from("direct:sendLoginEmail")
//        .routeId("sendLoginEmailRoute")
//        .routePolicy(routePolicy)
//        .setHeader("subject", constant("Login Notification"))
//        .setBody(simple("You have successfully logged in at ${header.loginTime}."))
//        .process(exchange -> globalEmailResources.incrementLoginEmailCounter())
//        .to("direct:send");
//
//    from("direct:sendVerificationEmail")
//        .routeId("sendVerificationEmailRoute")
//        .routePolicy(routePolicy)
//        .setHeader("subject", constant("Account Verification"))
//        .process(exchange -> globalEmailResources.incrementVerificationEmailCounter())
//        .to("direct:send");
//
//    from("direct:send")
//        .routeId("sendRoute")
//        .setHeader("to", simple("${header.to}"))
//        .toD(
//            "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
//                + "&password={{spring.mail.password}}&mail.smtp.auth=true"
//                + "&mail.smtp.starttls.enable=true")
//        .process(exchange -> globalEmailResources.incrementEmailCounter())
//        .log(
//            "\"${header.topic}\" email successfully sent to ${header.to} and counters"
//                + " increased.");
  }
}

//class RoutePolicy extends RoutePolicySupport {
//  private AtomicInteger counter = new AtomicInteger();
//
//  @Override
//  public void onExchangeBegin(Route route, Exchange exchange) {
//    exchange.setProperty("counter", counter);
//  }
//
//  public int getCounter() {
//    return counter.get();
//  }
//}
