package it.krisopea.springcors.route;

import it.krisopea.springcors.util.GlobalEmailResources;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailLogRoute extends RouteBuilder {
  @Autowired private GlobalEmailResources globalEmailResources;

  @Override
  public void configure() {
    from("timer:emailLogTimer")
        .routeId("emailLogTimer")
        .delayer(60000L)
        .log(
            "Sono state inviate "
                + globalEmailResources.getEmailCounter()
                + " email fin'ora in questa sessione.\n"
                + "In particolare: \n"
                + globalEmailResources.getRegistrationEmailCounter()
                + " email di registrazione, \n"
                + globalEmailResources.getUpdateEmailCounter()
                + " email di aggiornamento, \n"
                + globalEmailResources.getDeleteEmailCounter()
                + " email di cancellazione. \n");
  }
}
