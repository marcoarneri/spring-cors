package it.krisopea.springcors.route;

import it.krisopea.springcors.util.GlobalEmailResources;
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
        .when(header("topic").isEqualTo("Registrazione utenza"))
        .setHeader("subject", constant("Benvenuto!"))
        .setBody(simple("Ciao, grazie per esserti registrato al nostro servizio!"))
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementRegistrationEmailCounter();
            })
        .when(header("topic").isEqualTo("Aggiornamento utenza"))
        .setHeader("subject", constant("Notifica di aggiornamento"))
        .setBody(
            simple("Vogliamo notificarti dell'aggiornamento di uno o più campi della tua utenza."))
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementUpdateEmailCounter();
            })
        .when(header("topic").isEqualTo("Cancellazione utenza"))
        .choice()
        .when(header("isAdmin").isEqualTo(Boolean.TRUE.toString()))
        .setHeader("subject", constant("Notifica di cancellazione"))
        .setBody(
            simple(
                "Vogliamo notificarti della cancellazione della tua utenza da parte di un"
                    + " amministratore."))
        .otherwise()
        .setHeader("subject", constant("Notifica di cancellazione"))
        .setBody(
            simple(
                "Vogliamo notificarti della cancellazione della tua utenza. Speriamo di rivederti"
                    + " presto!"))
        .end()
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementDeleteEmailCounter();
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
