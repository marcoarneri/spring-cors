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
        .setHeader("subject", constant("Welcome!"))
        .setBody(simple("Hi, thank you for registering to our service!"))
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementRegistrationEmailCounter();
            })
        .when(header("topic").isEqualTo(EmailEnum.UPDATE))
        .setHeader("subject", constant("Update Notification"))
        .setBody(
            simple(
                "We would like to notify you about the update of one or more fields of your"
                    + " account."))
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementUpdateEmailCounter();
            })
        .when(header("topic").isEqualTo(EmailEnum.DELETE))
        .choice()
        .when(header("isAdmin").isEqualTo(Boolean.TRUE.toString()))
        .setHeader("subject", constant("Cancellation Notification"))
        .setBody(
            simple(
                "We want to notify you of the cancellation of your account by an administrator."))
        .otherwise()
        .setHeader("subject", constant("Cancellation Notification"))
        .setBody(
            simple(
                "We want to notify you of the cancellation of your account. We hope to see you"
                    + " again soon!"))
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementDeleteEmailCounter();
            })
        .when(header("topic").isEqualTo(EmailEnum.LOGIN))
        .setHeader("subject", constant("Login Notification"))
        .setBody(simple("You have successfully logged in at ${header.loginTime}."))
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementLoginEmailCounter();
            })
        .end()
        .setHeader("to", simple("${header.email}"))
        .toD(
            "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                + "&password={{spring.mail.password}}&mail.smtp.auth=true"
                + "&mail.smtp.starttls.enable=true")
        .log(
            "\"${header.topic}\" email successfully sent to ${header.email} and counters"
                + " increased.");
  }
}
