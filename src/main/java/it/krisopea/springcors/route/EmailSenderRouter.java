package it.krisopea.springcors.route;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.util.GlobalEmailResources;
import it.krisopea.springcors.util.constant.EmailEnum;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

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
            .to("direct:send")
            .when(header("topic").isEqualTo(EmailEnum.UPDATE))
            .to("direct:sendUpdateEmail")
            .when(header("topic").isEqualTo(EmailEnum.DELETE))
            .to("direct:sendDeleteEmail")
            .when(header("topic").isEqualTo(EmailEnum.LOGIN))
            .to("direct:sendLoginEmail")
            .end();


//        from("direct:send")
//            .routeId("sendRoute")
//            .setHeader("to", simple("${header.to}"))
//                .setBody(simple("Inserisci il token nella pagina di verificazione: ${header.token}"))
//            .toD(
//
//     "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
//                    + "&password={{spring.mail.password}}&mail.smtp.auth=true"
//                    + "&mail.smtp.starttls.enable=true")
//            .process(exchange -> globalEmailResources.incrementEmailCounter())
//            .log(
//                "\"${header.topic}\" email successfully sent to ${header.to} and counters"
//                    + " increased.");

    from("direct:send")
            .routeId("sendRoute")
            .setHeader("to", simple("${header.to}"))
            .setHeader("subject", constant("Welcome!"))
            .setBody(simple("Click on link to verify your email: http://localhost:{{server.port}}/linkVerify?username=${header.username}&token=${header.token}"))
            .toD("smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                            + "&password={{spring.mail.password}}&mail.smtp.auth=true"
                            + "&mail.smtp.starttls.enable=true")
            .process(exchange -> globalEmailResources.incrementEmailCounter())
            .log("\"${header.topic}\" email successfully sent to ${header.to} and counters"
                            + " increased.");
  }
}