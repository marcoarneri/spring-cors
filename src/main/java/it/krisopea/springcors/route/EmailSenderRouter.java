package it.krisopea.springcors.route;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.util.constant.EmailEnum;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderRouter extends RouteBuilder {

  @Autowired private UserRepository userRepository;
  @Autowired private VerificationRepository verificationRepository;

  @Override
  public void configure() {
    onException(Exception.class)
            .handled(true)
            .log(LoggingLevel.ERROR, "Error while sending the email: ${exception.message}")
            .process(exchange -> {
              throw new Exception("Error while sending the email");
            });

    from("direct:sendEmail")
            .routeId("sendEmailRoute")
            .choice()
            .when(header("topic").isEqualTo(EmailEnum.REGISTRATION))
            .to("direct:send")
            .when(header("topic").isEqualTo(EmailEnum.UPDATE))
            .to("direct:sendUpdateEmail")
            .when(header("topic").isEqualTo(EmailEnum.DELETE))
            .to("direct:sendDeleteEmail")
            .when(header("topic").isEqualTo(EmailEnum.CHANGE_PASSWORD))
            .to("direct:changePassword")
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
            .setHeader("subject", constant("Verify your E-mail!"))
            .setBody(simple("Click on link to verify your email: http://localhost:{{server.port}}/linkVerify?token=${header.token}"))
            .toD("smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                            + "&password={{spring.mail.password}}&mail.smtp.auth=true"
                            + "&mail.smtp.starttls.enable=true")
            .log("\"${header.topic}\" email successfully sent to ${header.to}");

    from("direct:changePassword")
            .routeId("changePassword")
            .setHeader("to", simple("${header.to}"))
            .setHeader("subject", constant("Change your password!"))
            .setBody(simple("Click on link to change your password: http://localhost:{{server.port}}/changePassword?id=${header.id}"))
            .toD("smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                    + "&password={{spring.mail.password}}&mail.smtp.auth=true"
                    + "&mail.smtp.starttls.enable=true")
            .log("\"${header.topic}\" email successfully sent to ${header.to}");
  }
}