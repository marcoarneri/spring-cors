package it.krisopea.springcors.route;

import it.krisopea.springcors.util.GlobalEmailResources;
import it.krisopea.springcors.util.constant.EmailEnum;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderRouter extends RouteBuilder {
  @Autowired private GlobalEmailResources globalEmailResources;

  @Override
  public void configure() {
    onException(Exception.class)
        .handled(true)
        .log(LoggingLevel.ERROR, "Error while sending the email: ${exception.message}");

    from("direct:sendEmail")
        .routeId("sendEmailRoute")
        .choice()
        .when(header("topic").isEqualTo(EmailEnum.REGISTRATION))
        .to("direct:sendVerification")
        //        // FIXME
        //        .when(header("topic").isEqualTo(EmailEnum.UPDATE))
        //        .to("direct:sendUpdateEmail")
        //        .when(header("topic").isEqualTo(EmailEnum.DELETE))
        //        .to("direct:sendDeleteEmail")
        //        .when(header("topic").isEqualTo(EmailEnum.LOGIN))
        //        .to("direct:sendLoginEmail")
        .end();

    from("direct:sendVerification")
        .routeId("sendVerificationRoute")
        .setHeader("to", simple("${header.to}"))
        .setHeader("subject", simple("Account Verification"))
        .setBody(
            simple(
                "Hi! Thanks for signing up to our service.\n Insert the token: ${header.token} \nOr"
                    + " click the following link to verify your account: \n")) // TODO link
        .toD(
            "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                + "&password={{spring.mail.password}}&mail.smtp.auth=true"
                + "&mail.smtp.starttls.enable=true")
        .process(exchange -> globalEmailResources.incrementEmailCounter())
        .log(
            "\"${header.topic}\" email successfully sent to ${header.to} and counters"
                + " increased.");
  }
}
