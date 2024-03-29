package it.krisopea.springcors.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DemoRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:processDemoResponse2")
                .process(exchange -> log.info("ciao ciao"));
    }
}
