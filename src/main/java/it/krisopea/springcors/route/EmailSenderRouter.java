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
        .log(
            LoggingLevel.ERROR,
            "Errore di timeout durante l'invio dell'email: ${exception.message}");

    from("direct:sendRegistrationEmail")
        .routeId("sendRegistrationEmailRoute")
        .setHeader("subject", constant("Benvenuto!"))
        .setHeader("to", simple("${header.email}"))
        .setBody(simple("Ciao, grazie per esserti registrato al nostro servizio!"))
        .toD(
            "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                + "&password={{spring.mail.password}}&mail.smtp.auth={{spring.mail.properties.mail.smtp.auth}}"
                + "&mail.smtp.starttls.enable={{spring.mail.properties.mail.smtp.starttls.enable}}")
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementRegistrationEmailCounter();
            })
        .log("Email di registrazione inviata con successo e contatori di sessione incrementati.");

    from("direct:sendUpdateEmail")
        .routeId("sendUpdateEmailRoute")
        .setHeader("subject", constant("Notifica di aggiornamento"))
        .setHeader("to", simple("${header.email}"))
        .setBody(
            simple("Vogliamo notificarti dell'aggiornamento di uno o più campi della tua utenza."))
        .toD(
            "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                + "&password={{spring.mail.password}}&mail.smtp.auth={{spring.mail.properties.mail.smtp.auth}}"
                + "&mail.smtp.starttls.enable={{spring.mail.properties.mail.smtp.starttls.enable}}")
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementUpdateEmailCounter();
            })
        .log("Email di aggiornamento inviata con successo e contatori di sessione incrementati.");

    from("direct:sendDeleteEmail")
        .routeId("sendDeleteEmailRoute")
        .setHeader("subject", constant("Notifica di cancellazione"))
        .setHeader("to", simple("${header.email}"))
        .setBody(
            simple(
                "Vogliamo notificarti della cancellazione della tua utenza. "
                    + "Speriamo di vederti di nuovo!"))
        .toD(
            "smtps://{{spring.mail.host}}:{{spring.mail.port}}?username={{spring.mail.username}}"
                + "&password={{spring.mail.password}}&mail.smtp.auth={{spring.mail.properties.mail.smtp.auth}}"
                + "&mail.smtp.starttls.enable={{spring.mail.properties.mail.smtp.starttls.enable}}")
        .process(
            exchange -> {
              globalEmailResources.incrementEmailCounter();
              globalEmailResources.incrementDeleteEmailCounter();
            })
        .log("Email di cancellazione inviata con successo e contatori di sessione incrementati.");
  }
}
