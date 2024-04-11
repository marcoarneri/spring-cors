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
        .delayer(210000)
        .log(
            "A total of "
                + globalEmailResources.getEmailCounter()
                + " emails have been sent so far in this session. "
                + "Specifically: "
                + globalEmailResources.getRegistrationEmailCounter()
                + " registrations, "
                + globalEmailResources.getLoginEmailCounter()
                + " logins, "
                + globalEmailResources.getUpdateEmailCounter()
                + " updates, "
                + globalEmailResources.getDeleteEmailCounter()
                + " deletions, "
                + globalEmailResources.getVerificationEmailCounter()
                + " verifications.");
  }
}
